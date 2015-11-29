package app.utils.dto;

import java.io.Serializable;

public class PieData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int tyleColor;
	private String contentData;

	public PieData() {
	}

	public PieData(int tyleColor, String contentData) {
		this.tyleColor = tyleColor;
		this.contentData = contentData;
	}

	public int getTyleColor() {
		return tyleColor;
	}

	public void setTyleColor(int tyleColor) {
		this.tyleColor = tyleColor;
	}

	public String getContentData() {
		return contentData;
	}

	public void setContentData(String contentData) {
		this.contentData = contentData;
	}

	public static void main(String[] args) {
		ListPieData listPie = new ListPieData();
		for (int i = 0; i < 8; i++) {
			PieData a = new PieData(i, "this is content of " + i);
			listPie.add(a);
		}
		
		System.out.println(listPie.pieDataToJson());
	}
	
}
