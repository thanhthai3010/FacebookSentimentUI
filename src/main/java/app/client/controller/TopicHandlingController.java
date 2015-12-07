package app.client.controller;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import app.utils.dto.FacebookData;
import app.utils.dto.JsonDataToDrawChart;
import app.utils.dto.ListReportData;
import app.utils.dto.ListTopic;
import app.utils.dto.PieChart;
import app.utils.dto.ReportData;

@Controller
public class TopicHandlingController {

	private static final Logger logger = LoggerFactory
			.getLogger(TopicHandlingController.class);

	private ListTopic describeTopics;
	
	/**
	 * Neutral Sentiment
	 */
	private static final int NEUTRAL = 0;

	/**
	 * Negative Sentiment
	 */
	private static final int NEGATIVE = -1;

	/**
	 * Positive Sentiment
	 */
	private static final int POSITIVE = 1;
	
	/**
	 * Process LDA and output topics
	 * @param model
	 * @param req
	 * @return
	 * @throws RemoteException
	 */
	@RequestMapping(value = "/startAnalysis", method = RequestMethod.POST)
	public String startAnalysisProcess(Model model, HttpServletRequest req,
			String pageID, String dateFrom, String dateTo)
			throws RemoteException {
		logger.info("View word cloud of topic");
		
		boolean errorFlag = false;
		
		String[] lstPageIDFromClient = pageID.split(",");
		
		List<String> lstPageID = new ArrayList<String>();
		for (String pageItem : lstPageIDFromClient) {
			lstPageID.add(pageItem);
		}
		//TODO
		// please check value of getFBDataByPageIDAndDate() method return.
		// If we does not have data, show message for user.
		FacebookData inputDataForService = new FacebookData();
		inputDataForService = HomeController.server.getFBDataByPageIDAndDate(
				lstPageID, dateFrom, dateTo);
		
		if (inputDataForService.getFbDataForService().isEmpty()) {
			errorFlag = true;
		}
		
		HomeController.server.processLDA(inputDataForService);

		if (errorFlag) {
			model.addAttribute("error", "Please select another data");
			return "analysisData";
		} else {
			return "viewTopics";
		}
	}
	
	/**
	 * Display topic word-cloud
	 * @param model
	 * @param req
	 * @return
	 * @throws RemoteException
	 */
	@ResponseBody
	@RequestMapping(value = "/getListTopic", method = RequestMethod.GET, produces = "text/html; charset=utf-8") 
	public String getListTopicsView(Model model, HttpServletRequest req) throws RemoteException {
		
		logger.info("Starting draw word-cloud");
		this.describeTopics = HomeController.server.getDescribleTopics();
		
		return describeTopics.toTopicsJson();
	}
	
	/**
	 * View detail of each topic by topicID
	 * @param model
	 * @param request
	 * @return
	 * @throws RemoteException
	 */
	@ResponseBody
	@RequestMapping(value = "/topicID", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
	public String getPieData(Model model, HttpServletRequest request)
			throws RemoteException {
		
		int topicID = 1;
		try {
			topicID = Integer.parseInt(request.getParameter("id"));
		} catch (Exception e) {
			logger.info("Has problem here");
		}
		// first of all, we need to get list of comment to process sentiment
		ListReportData affterSentiment = HomeController.server.processSentiment(topicID);
		
		/**
		 * get data to draw pie chart
		 */
		List<PieChart> listPieChart = getCharData(affterSentiment);
		
		// convert to JSon
		String listDetailData = affterSentiment.pieDataToJson();
		
		String pieChartJson = PieChart.pieChartsToJson(listPieChart);
		
		JsonDataToDrawChart jsonData = new JsonDataToDrawChart(listDetailData, pieChartJson);
		
		return jsonData.toJsonData();

	}
	
	@RequestMapping(value = "/pieChart", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
	public String callDrawPieChart(Model model, HttpServletRequest request)
			throws RemoteException {
		
		int topicID = 1;
		try {
			topicID = Integer.parseInt(request.getParameter("id"));
		} catch (Exception e) {
			logger.info("Has problem here");
		}
		model.addAttribute("topicID", topicID);
		
		
		return "detailOfTopic";

	}
	
	/**
	 * Object to draw pie chart
	 * @param lisPieData
	 * @return
	 */
	public List<PieChart> getCharData(ListReportData lisPieData){
		// loop all pieData input to calculate sum of each type color
		
		List<PieChart> lstPieChar = new ArrayList<PieChart>();
		
		int numOfPos = 0;
		int numOfNeg = 0;
		int numOfNeu = 0;
		for (ReportData pieData : lisPieData) {
			switch (pieData.getTypeColor()) {
			case POSITIVE:
				numOfPos++;
				break;
			case NEGATIVE:
				numOfNeg++;
				break;
			case NEUTRAL: 
				numOfNeu++;
				break;
			default:
				break;
			}
		}
		
		PieChart piePositive = new PieChart("Positive Percent", numOfPos);
		PieChart pieNegative = new PieChart("Negative Percent", numOfNeg);
		PieChart pieNeutral = new PieChart("Neutral Percent", numOfNeu);
		
		lstPieChar.add(piePositive);
		lstPieChar.add(pieNegative);
		lstPieChar.add(pieNeutral);
		
		return lstPieChar;
	}
}
