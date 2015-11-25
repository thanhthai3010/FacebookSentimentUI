package app.utils.dto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;


public class ListTopic extends ArrayList<Topic> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String toTopicsJson(){
		return new Gson().toJson(this);
	}
	
}
