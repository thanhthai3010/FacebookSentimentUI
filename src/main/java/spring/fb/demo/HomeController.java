package spring.fb.demo;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
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

import app.server.handling.ServerInterf;
import app.utils.dto.FacebookData;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.Comment;
import com.restfb.types.Post;
import com.restfb.types.User;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory
			.getLogger(HomeController.class);

	private static final int MAX_POST_LIMITED = 50;

	private FacebookClient facebookClient23;
	public static ServerInterf server;
	private Registry myRegis;

	// THAINT
	private FacebookData inputDataForService;

	/**
	 * String is blank
	 */
	private static final String STRING_BLANK = "";
	
	/**
	 * String space
	 */
	private static final String STRING_SPACE = " ";

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {
		logger.info("Welcome home!");
		try {
			if (myRegis == null) {
				myRegis = LocateRegistry.getRegistry("127.0.0.1");
			}
			if (server == null) {
				server = (ServerInterf) myRegis.lookup("server");
			}
			if (server != null) {
				logger.info("qtran: " + server.hello());
			} else {
				return "error";
			}
		} catch (RemoteException e) {
			server = null;
			logger.info("(RemoteException) Failed to find server.");
			return "error";
		} catch (NotBoundException e) {
			server = null;
			logger.info("(NotBoundException) Failed to find server.");
			return "error";
		}
		return "home";
	}

	@RequestMapping(value = "/submitAccessToken", method = RequestMethod.POST)
	public String ProcessAccessToken(Model model, HttpServletRequest req)
			throws RemoteException {
		logger.info("Process form");

		HashMap<String, Connection<Post>> pagesPosts = new HashMap<String, Connection<Post>>();

		String userAT = req.getParameter("userAccessToken");
		String pageID = req.getParameter("pageID");

		facebookClient23 = new DefaultFacebookClient(userAT,
				Version.VERSION_2_3);
		Connection<Post> listPosts;

		if (pageID == null || pageID == STRING_BLANK) {

			// get user's feed
			listPosts = facebookClient23.fetchConnection("me/feed", Post.class,
					Parameter.with("limit", MAX_POST_LIMITED));
			Connection<User> user = facebookClient23.fetchConnection("me",
					User.class, Parameter.with("fields", "id,name"));
			if (user != null && user.getData().size() > 0) {
				String userName = user.getData().get(0).getFirstName() + STRING_SPACE
						+ user.getData().get(0).getLastName();
				req.setAttribute("userID", userName);
			}
		} else {

			String listPageID[] = pageID.split(",");

			for (String idItem : listPageID) {
				// TODO: need to add parameter: since date value
				listPosts = facebookClient23.fetchConnection(idItem.trim()
						+ "/feed", Post.class,
						Parameter.with("limit", MAX_POST_LIMITED),
						Parameter.with("since", "11/01/2015"));
				pagesPosts.put(idItem, listPosts);
			}
		}

		// TODO
		this.inputDataForService = new FacebookData();
		inputDataForService = getFBDataForService(pagesPosts);
		server.processLDA(inputDataForService);

		return "viewTopics";
	}

	/**
	 * Convert input from font Helvetica to UTF-8 format
	 * 
	 * @param input String
	 * @return String after convert to UTF-8 String
	 */
	public String covertStr(String input) {
		if (input != null && !STRING_BLANK.equals(input)) {
			
			input = input.replaceAll("#\\s*(\\w+)", "");
			input = input.replace("á", "á");
			input = input.replace("ố", "ố");
			input = input.replace("ể", "ể");
			input = input.replace("ọ", "ọ");
			input = input.replace("ủ", "ủ");
			input = input.replace("ạ", "ạ");
			input = input.replace("ẽ", "ẽ");
			input = input.replace("ẻ", "ẻ");
			input = input.replace("ấ", "ấ");
			input = input.replace("ự", "ự");
			input = input.replace("ụ", "ụ");
			input = input.replace("ó", "ó");
			input = input.replace("ỹ", "ỹ");
			input = input.replace("ớ", "ớ");
			input = input.replace("ợ", "ợ");
			input = input.replace("ì", "ì");
			input = input.replace("ị", "ị");
			input = input.replace("à", "à");
			input = input.replace("í", "í");
			input = input.replace("ờ", "ờ");
			input = input.replace("ặ", "ặ");
			input = input.replace("ằ", "ằ");
			input = input.replace("ệ", "ệ");
			input = input.replace("ế", "ế");
			input = input.replace("ề", "ề");
			input = input.replace("ổ", "ổ");
			input = input.replace("ẳ", "ẳ");
			input = input.replace("ứ", "ứ");
			input = input.replace("ả", "ả");
			input = input.replace("ù", "ù");
			input = input.replace("ỏ", "ỏ");
			input = input.replace("ỗ", "ỗ");
			input = input.replace("ỉ", "ỉ");
			input = input.replace("ò", "ò");
			input = input.replace("ễ", "ễ");
			input = input.replace("ú", "ú");
			input = input.replace("ầ", "ầ");
			input = input.replace("ã", "ã");
			input = input.replace("ẫ", "ẫ");
			input = input.replace("ữ", "ữ");
			input = input.replace("ồ", "ồ");
			input = input.replace("ẩ", "ẩ");
			input = input.replace("ừ", "ừ");
			input = input.replace("ũ", "ũ");
			input = input.replace("ý", "ý");
			input = input.replace("ử", "ử");
			input = input.replace("ộ", "ộ");
			input = input.replace("ậ", "ậ");
			input = input.replace("ở", "ở");
			input = input.replace("ĩ", "ĩ");
			input = input.replace("ắ", "ắ");
			input = input.replace("ỡ", "ỡ");
		}

		return input;
	}

	/**
	 * get facebook status and all of comment for Service process
	 * @param pagesPosts List of facebook
	 * @return data for Service process
	 */
	public FacebookData getFBDataForService(
			Map<String, Connection<Post>> pagesPosts) {

		FacebookData returnData = new FacebookData();

		Map<String, List<String>> inputData = new LinkedHashMap<String, List<String>>();

		for (Entry<String, Connection<Post>> item : pagesPosts.entrySet()) {
			// get each page
			for (Post post : item.getValue().getData()) {
				List<String> listComment = new ArrayList<String>();
				if (post.getComments() != null) {
					for (Comment cmt : post.getComments().getData()) {
						String cmtMessage = cmt.getMessage();
						if (cmtMessage != null
								&& !STRING_BLANK.equals(cmtMessage)
								&& !STRING_SPACE.equals(cmtMessage)) {
							listComment.add(covertStr(cmtMessage));
						}
					}
				}
				// Return data <Status, List<Comment of this Status>>
				String postMessage = post.getMessage();
				if (postMessage != null && !STRING_BLANK.equals(postMessage)
						&& !STRING_SPACE.equals(postMessage)) {
					inputData.put(covertStr(postMessage), listComment);
				}
			}
		}

		// Set data for DTO to transfer to Service
		returnData.setFbDataForService(inputData);

		return returnData;
	}

	@ResponseBody
	@RequestMapping(value = "/topicID", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
	public String getPieData(Model model, HttpServletRequest request)
			throws RemoteException {

		// first of all, we need to get list of comment to process sentiment

		return null;

	}
}
