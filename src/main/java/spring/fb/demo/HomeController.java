package spring.fb.demo;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import app.server.handling.ServerInterf;
import app.utils.dto.InputDataForLDA;
import app.utils.dto.PostData;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
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
	private ServerInterf server;
	private Registry myRegis;

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
				System.out.println("qtran: " + server.hello());
				System.out
						.println("This is a demo: "
								+ server.runAnalyzeSentiment(
										"Hôm nay anh không vui và cũng không hạnh phúc tí nào em à!",
										true));
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

		if (pageID == null || pageID == "") {

			// get user's feed
			listPosts = facebookClient23.fetchConnection("me/feed", Post.class,
					Parameter.with("limit", MAX_POST_LIMITED));
			Connection<User> user = facebookClient23.fetchConnection("me",
					User.class, Parameter.with("fields", "id,name"));
			if (user != null && user.getData().size() > 0) {
				String userName = user.getData().get(0).getFirstName() + " "
						+ user.getData().get(0).getLastName();
				req.setAttribute("userID", userName);
			}
		} else {

			String listPageID[] = pageID.split(",");

			for (String idItem : listPageID) {
				listPosts = facebookClient23.fetchConnection(idItem.trim()
						+ "/feed", Post.class,
						Parameter.with("limit", MAX_POST_LIMITED));
				pagesPosts.put(idItem, listPosts);
			}
		}

		List<PostData> listPostDatas = new ArrayList<>();
		PostData postData;

		// ThaiNT
		InputDataForLDA inputDataForLDA = new InputDataForLDA();
		// TODO
		List<String> listOfPostFBForLDA = new ArrayList<String>();

		for (Entry<String, Connection<Post>> item : pagesPosts.entrySet()) {
			System.out.println("Working with page: " + item.getKey());
			for (Post post : item.getValue().getData()) {
				postData = new PostData();
				postData.setCaption(post.getCaption() != null ? post
						.getCaption()/* .replaceAll("\\d","") */: "");
				postData.setComments(post.getComments());
				postData.setCreatedTime(post.getCreatedTime());
				postData.setDescription(post.getDescription() != null ? post
						.getDescription()/* .replaceAll("\\d","") */: "");
				postData.setLikes(post.getLikes());
				postData.setLikesCount(post.getLikesCount());
				postData.setMessage(post.getMessage() != null ? post
						.getMessage()/* .replaceAll("\\d","") */: "");
				postData.setPostID(post.getId());
				postData.setStory(post.getStory() != null ? post.getStory()
						: "");
				postData.setUpdatedTime(post.getUpdatedTime());
				try {
					StringBuffer bufferItem = new StringBuffer();

					bufferItem
							.append(post.getDescription() != null ? covertStr(removeNumber(postData
									.getDescription())) : "");
					bufferItem
							.append(post.getMessage() != null ? covertStr(removeNumber(post
									.getMessage())) : "");
					bufferItem
							.append(post.getCaption() != null ? covertStr(postData
									.getCaption()) : "");
					bufferItem
							.append(post.getStory() != null ? covertStr(postData
									.getStory()) : "");

					// analyze all comments
					/*
					 * if (post.getCommentsCount() > 0) {
					 * 
					 * for (Comment cmt : post.getComments().getData()) {
					 * bufferItem.append(". ");
					 * bufferItem.append(cmt.getMessage()); if
					 * (cmt.getCommentCount() > 0) { for (Comment subCmt :
					 * cmt.getComments().getData()) { bufferItem.append(". ");
					 * if(subCmt.getMessage() != null){
					 * bufferItem.append(subCmt.
					 * getMessage().replaceAll("\\d","")); } } } } }
					 */
					// postData.setSentimentScore(server.runAnalyzeSentiment(bufferItem.toString(),
					// true));

					listOfPostFBForLDA.add(bufferItem.toString());

				} catch (Exception e) {
					e.printStackTrace();
					req.getSession()
							.setAttribute("errorDetail", e.getMessage());
					return "error";
				}

				listPostDatas.add(postData);
			}
		}

		model.addAttribute("listPostDatas", listPostDatas);

		// TODO
		inputDataForLDA.setListOfPostFBForLDA(listOfPostFBForLDA);
		server.processLDA(inputDataForLDA);

		return "home";
	}

	public String removeNumber(String input) {
		return input = input.replaceAll("[0-9]", "");
	}

	public String covertStr(String input) {

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

		return input;
	}

}
