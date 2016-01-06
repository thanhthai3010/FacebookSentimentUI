package app.client.controller;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import app.utils.dto.Comment_Data;
import app.utils.dto.FacebookDataToInsertDB;
import app.utils.dto.Page_Info;
import app.utils.dto.Post_Data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.Comment;
import com.restfb.types.Page;
import com.restfb.types.Post;

@Controller
public class GetFacebookDataController {

	private static final Logger logger = LoggerFactory
			.getLogger(GetFacebookDataController.class);

	/**
	 * Facebook library
	 */
	private FacebookClient facebookClient23;

	/**
	 * String is blank
	 */
	private static final String STRING_BLANK = "";

	/**
	 * String space
	 */
	private static final String STRING_SPACE = " ";

	/**
	 * Stored list pageID input from webUI
	 */
	private String[] listPageID;

	/**
	 * Call method save data
	 * 
	 * @param model
	 * @param req
	 * @return message
	 * @throws RemoteException
	 */
	@RequestMapping(value = "/saveFBData", method = RequestMethod.POST)
	public @ResponseBody String saveFBData(Model model, HttpServletRequest req)
			throws RemoteException {
		logger.info("Process form");

		/**
		 * response result to web
		 */
		String response = STRING_BLANK;

		/**
		 * Store <PageID, List<Post> data>
		 */
		Map<String, List<List<Post>>> hsMapFBData = new LinkedHashMap<String, List<List<Post>>>();

		/**
		 * Store facebook page information
		 */
		List<Page_Info> listFanPage = new ArrayList<Page_Info>();

		/**
		 * Get parameter from request of client
		 */
		String fbParameters = req.getParameter("0");
		JsonObject fbParamObj = new Gson().fromJson(fbParameters,
				JsonObject.class);

		// get Access Token
		String userAT = fbParamObj.get("userAccessToken").getAsString();
		// get pageID
		String pageID = fbParamObj.get("pageID").getAsString();
		// get date value
		String inputDate = fbParamObj.get("inputDate").getAsString();

		facebookClient23 = new DefaultFacebookClient(userAT,
				Version.VERSION_2_3);

		this.listPageID = pageID.split(",");

		for (String itemPageID : listPageID) {
			// Get Confession page name
			Page fanPage = facebookClient23
					.fetchObject(
							itemPageID.trim(),
							Page.class,
							Parameter.with("fields",
											"name,about,description,website,picture.type(large)"));
			logger.info("working with " + fanPage.getName());

			/**
			 * Add each pageInfo to List and then insert into database
			 */
			Page_Info pageInfo = new Page_Info(Long.parseLong(itemPageID),
					fanPage.getName(), fanPage.getPicture().getUrl(),
					fanPage.getAbout(), fanPage.getDescription(),
					fanPage.getWebsite());
			listFanPage.add(pageInfo);

			// TODO: need to add parameter: since date value
			// Store list of post foreach page
			List<List<Post>> pagesPosts = new ArrayList<List<Post>>();

			Connection<Post> listPostsFirst = facebookClient23.fetchConnection(
					itemPageID.trim() + "/feed", Post.class,
					Parameter.with("since", inputDate));

			// New
			pagesPosts.add(listPostsFirst.getData());
			String nextPage = listPostsFirst.getNextPageUrl();
			while (nextPage != null) {
				Connection<Post> listPostsContinous;
				listPostsContinous = facebookClient23.fetchConnectionPage(
						nextPage, Post.class);
				// Check is the last page
				if (listPostsContinous.getData().size() > 0) {
					pagesPosts.add(listPostsContinous.getData());
				}
				// Get URL of next page
				nextPage = listPostsContinous.getNextPageUrl();
			}
			;
			// Put data
			hsMapFBData.put(itemPageID, pagesPosts);
		}

		try {
			// save facebook data
			saveFBData(hsMapFBData);
			// save PAGE_INFO
			savePageInfo(listFanPage);
			response = "Get Facebook data successful!";
			logger.info("done get fb data!");
		} catch (SQLException e) {
			logger.info(e.getMessage());
			response = "Can't get data from Facebook";
		}
		return new Gson().toJson(response);
	}

	/**
	 * get facebook status and all of comment for Service process
	 * 
	 * @param pagesPosts
	 *            List of facebook
	 * @return data for Service process
	 * @throws SQLException
	 */
	public void saveFBData(Map<String, List<List<Post>>> hsMapData)
			throws SQLException {

		List<Post_Data> listPost_Data = new ArrayList<Post_Data>();

		List<Comment_Data> listComment_Data = new ArrayList<Comment_Data>();

		int postIdx = 0;
		int cmIdx = 0;
		for (Entry<String, List<List<Post>>> item : hsMapData.entrySet()) {
			// get each page
			for (List<Post> lstPost : item.getValue()) {

				// Foreach Post
				for (Post post : lstPost) {
					if (post.getComments() != null) {
						for (Comment cmt : post.getComments().getData()) {
							String cmtMessage = cmt.getMessage();
							if (cmtMessage != null
									&& !STRING_BLANK.equals(cmtMessage)
									&& !STRING_SPACE.equals(cmtMessage)) {
								Comment_Data cmData = new Comment_Data(
										Long.parseLong(item.getKey()), postIdx,
										cmIdx,
										replaceSpecialCharacters(cmtMessage));
								listComment_Data.add(cmData);
							}

							// incre cmIdx
							cmIdx++;
						}
					}

					// Return data <Status, List<Comment of this Status>>
					String postMessage = post.getMessage();

					// SAVE POST_DATA
					if (postMessage != null
							&& !STRING_BLANK.equals(postMessage)
							&& !STRING_SPACE.equals(postMessage)) {

						String dateStr = post.getCreatedTime().toString();
						DateFormat formatter = new SimpleDateFormat(
								"E MMM dd HH:mm:ss Z yyyy");

						String dateTime = STRING_BLANK;
						try {
							java.util.Date date = (java.util.Date) formatter
									.parse(dateStr);

							Calendar cal = Calendar.getInstance();
							cal.setTime(date);
							dateTime = cal.get(Calendar.YEAR) + "-"
									+ (cal.get(Calendar.MONTH) + 1) + "-"
									+ cal.get(Calendar.DATE);
						} catch (ParseException e) {
							logger.info(e.getMessage());
						}

						Post_Data postDT = new Post_Data(Long.parseLong(item
								.getKey()), postIdx,
								replaceSpecialCharacters(postMessage), dateTime);
						listPost_Data.add(postDT);
					}

					// incre postIdx
					postIdx++;
				}
			}
		}
		FacebookDataToInsertDB fbDataToInsertDB = new FacebookDataToInsertDB(
				listPost_Data, listComment_Data, this.listPageID);
		try {
			HomeController.server.saveFBData(fbDataToInsertDB);
		} catch (RemoteException e) {
			logger.info("Get error when insert FB database");
		}

	}

	/**
	 * save facebook page info
	 * 
	 * @param listFanPage
	 */
	private void savePageInfo(List<Page_Info> listFanPage) {
		try {
			HomeController.server.savePageInfo(listFanPage);
		} catch (RemoteException e) {
			logger.info("Get error when insert PAGE_INFO");
		}
	}

	/**
	 * Convert input from font Helvetica to UTF-8 format
	 * 
	 * @param input
	 *            String
	 * @return String after convert to UTF-8 String
	 */
	public String replaceSpecialCharacters(String input) {
		if (input != null && !STRING_BLANK.equals(input)) {

			// input = input.replaceAll("\\p{C}", " ");
			input = input.replaceAll("\n", " ");
			input = input.replaceAll("\r", " ");
			input = input.replaceAll("#\\s*(\\w+)", " ");
		}

		return input;
	}

	// public void testFunc(){
	// facebookClient23 = new DefaultFacebookClient(
	// "CAACEdEose0cBADEDT3COZBTZAA3QWCUYyyPZAWI6g8Symrk9lw5WEbqJ5W0wHfOLkSX1ZBx0TeYX3lEexegTnBseVKGYttmo44dpfR55Bd5q9Ir44D7FxdsaM7pB8lHRXBWOdQ53C5X2varUs7XT8KYAI1KYiH9ow45ipvOZBrJIzGOUxn6onpLR4zFD8n01OUN6q0OvoZAQZDZD",
	// Version.VERSION_2_3);
	// Connection<Post> listPostsFirst = facebookClient23.fetchConnection(
	// "447498478655695_958920147513523".trim() + "/comments", Post.class);
	// List<Post> lstPost = listPostsFirst.getData();
	// for (Post post : lstPost) {
	// System.out.println(post.getMessage());
	// }
	// }
	//
	// public static void main(String[] args) {
	// GetFacebookDataController sd = new GetFacebookDataController();
	// sd.testFunc();
	// }
}
