package app.utils.dto;

import java.io.Serializable;
import com.google.gson.Gson;

public class JsonDataToDrawChart implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Store detail data and theirs color
	 */
	private String listDetailData;

	/**
	 * Store data to draw pie chart
	 */
	private String listPieChart;

	/**
	 * default constructor
	 */
	public JsonDataToDrawChart() {
		
	}

	/**
	 * Constructor with parameter
	 * @param listDetailData
	 * @param listPieChart
	 */
	public JsonDataToDrawChart(String listDetailData, String listPieChart) {
		this.listDetailData = listDetailData;
		this.listPieChart = listPieChart;
	}


	/**
	 * get listPieChart
	 * @return listPieChart
	 */
	public String getListPieChart() {
		return listPieChart;
	}

	/**
	 * listPieChart
	 * @param listPieChart
	 */
	public void setListPieChart(String listPieChart) {
		this.listPieChart = listPieChart;
	}

	/**
	 * get listDetailData
	 * @return listDetailData
	 */
	public String getListDetailData() {
		return listDetailData;
	}

	/**
	 * set listDetailData
	 * @param listDetailData
	 */
	public void setListDetailData(String listDetailData) {
		this.listDetailData = listDetailData;
	}

	/**
	 * Convert this object to JSon.
	 * @return JSon
	 */
	public String toJsonData() {
		return new Gson().toJson(this);
	}
	
}
