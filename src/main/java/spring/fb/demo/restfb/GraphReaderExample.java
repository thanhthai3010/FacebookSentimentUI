/**
 * Copyright (c) 2010-2015 Mark Allen, Norbert Bartels.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package spring.fb.demo.restfb;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spring.fb.demo.SystemConstant;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.DefaultJsonMapper;
import com.restfb.Facebook;
import com.restfb.FacebookClient;
import com.restfb.JsonMapper;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.json.JsonArray;
import com.restfb.json.JsonObject;
import com.restfb.types.Page;
import com.restfb.types.Post;
import com.restfb.types.Url;
import com.restfb.types.User;

/**
 * Examples of RestFB's Graph API functionality.
 * 
 * @author <a href="http://restfb.com">Mark Allen</a>
 */
@SuppressWarnings("deprecation")
public class GraphReaderExample extends Example {
	/**
	 * RestFB Graph API client.
	 */
	private final FacebookClient facebookClient23;
	private final FacebookClient facebookClient20;

	private File fileDir;
	private Writer writerOut;

	/**
	 * Entry point. You must provide a single argument on the command line: a
	 * valid Graph API access token.
	 * 
	 * @param args
	 *            Command-line arguments.
	 * @throws IllegalArgumentException
	 *             If no command-line arguments are provided.
	 */
	public static void main(String[] args) {
		/*
		 * if (args.length == 0) throw new IllegalArgumentException(
		 * "You must provide an OAuth access token parameter. " +
		 * "See README for more information.");
		 */
		new GraphReaderExample(SystemConstant.ACCESS_TOKEN).runEverything();
	}

	private boolean initWriter() {
		try {
			fileDir = new File("c:\\temp\\test.txt");

			writerOut = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileDir, false), "UTF8"));

			if (writerOut != null) {
				return true;
			}

			/*
			 * out.append("Website UTF-8").append("\r\n");
			 * out.append(user.getName()).append("\r\n"); out.append(" " +
			 * page.getLikes()).append("\r\n");
			 * 
			 * out.flush(); out.close();
			 */

		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	private void writeToFile(String content) {
		if (writerOut != null) {
			try {
				writerOut.append(content).append("\r\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public GraphReaderExample(String accessToken) {
		facebookClient23 = new DefaultFacebookClient(accessToken,
				Version.VERSION_2_3);
		facebookClient20 = new DefaultFacebookClient(accessToken,
				Version.VERSION_2_0);
		// qtran - added initial
		initWriter();
	}

	void runEverything() {
		// fetchObject();
		// fetchObjects();
		// fetchObjectsAsJsonObject();
		// fetchConnections();
		// fetchDifferentDataTypesAsJsonObject();
		// query();
		// multiquery();
		// search();
		// metadata();
		paging();
		// selection();
		// parameters();
		// rawJsonResponse();
		try {
			writerOut.flush();
			writerOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void fetchObject() {
		out.println("* Fetching single objects *");

		User user = facebookClient23.fetchObject("me", User.class);
		Page page = facebookClient23.fetchObject("cocacola", Page.class);

		out.println("User name: " + user.getName());
		out.println("Page likes: " + page.getLikes());
	}

	void fetchObjectsAsJsonObject() {
		out.println("* Fetching multiple objects at once as a JsonObject *");

		List<String> ids = new ArrayList<String>();
		ids.add("4");
		ids.add("http://www.imdb.com/title/tt0117500/");

		// Make the API call
		JsonObject results = facebookClient23.fetchObjects(ids,
				JsonObject.class);

		System.out.println(results.toString(3));

		// Pull out JSON data by key and map each type by hand.
		JsonMapper jsonMapper = new DefaultJsonMapper();
		User user = jsonMapper.toJavaObject(results.getString("4"), User.class);
		Url url = jsonMapper.toJavaObject(
				results.getString("http://www.imdb.com/title/tt0117500/"),
				Url.class);

		out.println("User is " + user);
		out.println("URL is " + url);
	}

	void fetchObjects() {
		out.println("* Fetching multiple objects at once *");

		FetchObjectsResults fetchObjectsResults = facebookClient23
				.fetchObjects(Arrays.asList("me", "cocacola"),
						FetchObjectsResults.class);

		out.println("User name: " + fetchObjectsResults.me.getName());
		out.println("Page likes: " + fetchObjectsResults.page.getLikes());
	}

	void fetchDifferentDataTypesAsJsonObject() {
		out.println("* Fetching different types of data as JsonObject *");

		JsonObject zuck = facebookClient23.fetchObject("4", JsonObject.class);
		out.println(zuck.getString("name"));

		JsonObject photosConnection = facebookClient23.fetchObject("me/photos",
				JsonObject.class);
		JsonArray photosConnectionData = photosConnection.getJsonArray("data");

		if (photosConnectionData.length() > 0) {
			String firstPhotoUrl = photosConnectionData.getJsonObject(0)
					.getString("source");
			out.println(firstPhotoUrl);
		}

		String query = "SELECT uid, name FROM user WHERE uid=4 or uid=11";
		List<JsonObject> queryResults = facebookClient20.executeFqlQuery(query,
				JsonObject.class);

		if (queryResults.size() > 0)
			out.println(queryResults.get(0).getString("name"));
	}

	/**
	 * Holds results from a "fetchObjects" call.
	 */
	public static class FetchObjectsResults {
		@Facebook
		User me;

		@Facebook(value = "cocacola")
		Page page;
	}

	void fetchConnections() {
		out.println("* Fetching connections *");

		Connection<User> myFriends = facebookClient23.fetchConnection(
				"me/friends", User.class);
		Connection<Post> myFeed = facebookClient23.fetchConnection("me/feed",
				Post.class);

		out.println("Count of my friends: " + myFriends.getData().size());

		if (myFeed.getData().size() > 0)
			out.println("First item in my feed: "
					+ myFeed.getData().get(0).getMessage());
	}

	void query() {
		out.println("* FQL Query *");

		List<FqlUser> users = facebookClient20.executeFqlQuery(
				"SELECT uid, name FROM user WHERE uid=4 or uid=11",
				FqlUser.class);

		out.println("User: " + users);
	}

	void multiquery() {
		out.println("* FQL Multiquery *");

		Map<String, String> queries = new HashMap<String, String>();
		queries.put("users", "SELECT uid, name FROM user WHERE uid=4 OR uid=11");
		queries.put("likers",
				"SELECT user_id FROM like WHERE object_id=122788341354");

		MultiqueryResults multiqueryResults = facebookClient20
				.executeFqlMultiquery(queries, MultiqueryResults.class);

		out.println("Users: " + multiqueryResults.users);
		out.println("People who liked: " + multiqueryResults.likers);
	}

	/**
	 * Holds results from an "executeQuery" call.
	 * <p>
	 * Be aware that FQL fields don't always map to Graph API Object fields.
	 */
	public static class FqlUser {
		@Facebook
		String uid;

		@Facebook
		String name;

		@Override
		public String toString() {
			return format("%s (%s)", name, uid);
		}
	}

	/**
	 * Holds results from an "executeQuery" call.
	 * <p>
	 * Be aware that FQL fields don't always map to Graph API Object fields.
	 */
	public static class FqlLiker {
		@Facebook("user_id")
		String userId;

		@Override
		public String toString() {
			return userId;
		}
	}

	/**
	 * Holds results from a "multiquery" call.
	 */
	public static class MultiqueryResults {
		@Facebook
		List<FqlUser> users;

		@Facebook
		List<FqlLiker> likers;
	}

	void search() {
		out.println("* Searching connections *");

		// Connection<Post> publicSearch =
		// facebookClient23.fetchConnection("search", Post.class,
		// Parameter.with("q", "watermelon"),
		// Parameter.with("type", "post"));

		Connection<User> targetedSearch = facebookClient23.fetchConnection(
				"search", User.class, Parameter.with("q", "Mark"),
				Parameter.with("type", "user"));

		// if (publicSearch.getData().size() > 0)
		// out.println("Public search: " +
		// publicSearch.getData().get(0).getMessage());

		out.println("Posts on my wall by friends named Mark: "
				+ targetedSearch.getData().size());
	}

	void metadata() {
		out.println("* Metadata *");

		User userWithMetadata = facebookClient23.fetchObject("me", User.class,
				Parameter.with("metadata", 1));

		out.println("User metadata: has albums? "
				+ userWithMetadata.getMetadata().getConnections().hasAlbums());
	}

	public Connection<Post> paging() {
		out.println("* Paging support *");

		/*Connection<User> myFriends = facebookClient23.fetchConnection(
				"me/friends", User.class);*/
		Connection<Post> myFeed = facebookClient23.fetchConnection("me/posts",
				Post.class, Parameter.with("limit", 100));

		if (myFeed.getData().size() > 0) {
			return myFeed;
		}
		return null;

		/*out.println("Count of my friends: " + myFriends.getData().size());

		if (myFeed.getData().size() > 0)
			writeToFile("First item in my feed: "
					+ myFeed.getData().get(0).getDescription());
		// out.println("First item in my feed: " + myFeed.getData().get(0));

		for (List<Post> myFeedConnectionPage : myFeed)
			for (Post post : myFeedConnectionPage)
				writeToFile("Post from my feed: " + post.getDescription());
		// out.println("Post from my feed: " + post);
		out.println("Paging done");*/
	}

	void selection() {
		out.println("* Selecting specific fields *");

		User user = facebookClient23.fetchObject("me", User.class,
				Parameter.with("fields", "id,name"));

		out.println("User name: " + user.getName());
	}

	void parameters() {
		out.println("* Parameter support *");

		Date oneWeekAgo = new Date(currentTimeMillis() - 1000L * 60L * 60L
				* 24L * 7L);

		Connection<Post> filteredFeed = facebookClient23.fetchConnection(
				"me/feed", Post.class, Parameter.with("limit", 3),
				Parameter.with("until", "yesterday"),
				Parameter.with("since", oneWeekAgo));

		out.println("Filtered feed count: " + filteredFeed.getData().size());
	}

	void rawJsonResponse() {
		out.println("* Raw JSON *");
		out.println("User object JSON: "
				+ facebookClient23.fetchObject("me", String.class));
	}
}