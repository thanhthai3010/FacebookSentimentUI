/**
 * 
 */
package app.utils.dto;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.restfb.types.Comment;
import com.restfb.types.Comments;
import com.restfb.types.Likes;

/**
 * @author AnhQuan
 *
 */
@Component
public class PostData {
	private String message;
	private String description;
	private String story;
	private String postID;
	private Double sentimentScore;
	private Comments comments;
	private Likes likes;
	private String caption;
	private Long likesCount;
	private Date createdTime;
	//Last comment
	private Date updatedTime;
	
	//TODO
	private List<String> input;
	
	public List<String> getInput() {
		return input;
	}
	public void setInput(List<String> input) {
		this.input = input;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStory() {
		return story;
	}
	public void setStory(String story) {
		this.story = story;
	}
	public String getPostID() {
		return postID;
	}
	public void setPostID(String postID) {
		this.postID = postID;
	}
	public Double getSentimentScore() {
		return sentimentScore;
	}
	public void setSentimentScore(Double sentimentScore) {
		this.sentimentScore = sentimentScore;
	}
	public Comments getComments() {
		return comments;
	}
	public void setComments(Comments comments) {
		this.comments = comments;
	}
	public Likes getLikes() {
		return likes;
	}
	public void setLikes(Likes likes) {
		this.likes = likes;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public Long getLikesCount() {
		return likesCount;
	}
	public void setLikesCount(Long likesCount) {
		this.likesCount = likesCount;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Date getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	
	

}
