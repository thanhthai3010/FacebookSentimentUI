package app.utils.dto;

import java.io.Serializable;

public class TextValueWordCloud implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String text;
	private Double value;

	public String getText() {
		return text;
	}

	public TextValueWordCloud() {
	}

	public TextValueWordCloud(String text, Double value) {
		super();
		this.text = text;
		this.value = value;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

}
