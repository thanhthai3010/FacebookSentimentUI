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
import app.utils.dto.Page_Info;
import app.utils.dto.PieChart;

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
		logger.info("Processing for /analysisData");
		
		try {
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
				Page_Info pageInfo = new Page_Info();
				try {
					pageInfo = HomeController.server.getPageInfoByPageID(pageID);
				} catch (RemoteException e) {
					logger.info("Have an error when getting list of page info");
				}
				String pageName = pageInfo.getPageName();
	
				model.addAttribute("pageName", pageName);
				model.addAttribute("dateFrom", dateFrom);
				model.addAttribute("dateTo", dateTo);
				
				model.addAttribute("pageID", lstPageID.get(0));
				return "viewTopics";
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return "error";
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
		logger.info("Processing for /getListTopic");
		try {
			logger.info("Starting draw word-cloud");
			this.describeTopics = HomeController.server.getDescribleTopics();
			return describeTopics.toTopicsJson();
		} catch (Exception ex){
			logger.error(ex.getMessage());
			return "error";
		}
	}
	
	//qtran
	@ResponseBody
	@RequestMapping(value = "/getListTopicTitle", method = RequestMethod.GET, produces = "text/html; charset=utf-8") 
	public String getListTopicsTitle(Model model, HttpServletRequest req) throws RemoteException {
		logger.info("Processing for /getListTopicTitle");
		try {
			logger.info("Starting draw word-cloud");
			if(this.describeTopics != null){
				return describeTopics.toTopicsJson();
			} else {
				return "error:error";
			}
		} catch (Exception ex){
			logger.error(ex.getMessage());
			return "error:error";
		}
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
		logger.info("Processing for /topicID");
		try {
			int topicID = 1;
			try {
				topicID = Integer.parseInt(request.getParameter("id"));
			} catch (Exception e) {
				logger.info("Has problem when get topicID");
			}
	
			// first of all, we need to get list of comment to process sentiment
			List<ListReportData> affterSentiment = HomeController.server.processSentiment(topicID);
			
			/**
			 * get data to draw pie chart
			 */
			List<PieChart> listPieChart = getCharData(affterSentiment);
			
			// convert to JSon
			String listDetailData = ListReportData.toJson(affterSentiment);
			
			String pieChartJson = PieChart.pieChartsToJson(listPieChart);
			
			JsonDataToDrawChart jsonData = new JsonDataToDrawChart(listDetailData, pieChartJson);
			return jsonData.toJsonData();
		} catch (Exception ex){
			logger.error(ex.getMessage());
			return "error";
		}

	}
	
	@RequestMapping(value = "/pieChart", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
	public String callDrawPieChart(Model model, HttpServletRequest request)
			throws RemoteException {
		logger.info("Processing for /pieChart");
		try {
			int topicID = 1;
			try {
				topicID = Integer.parseInt(request.getParameter("id"));
			} catch (Exception e) {
				logger.info("Has problem here");
			}
			model.addAttribute("topicID", topicID);
			
			String pageID = "";
			Page_Info pageInfo = new Page_Info();
			try {
				pageID = request.getParameter("pageID");
				try {
					pageInfo = HomeController.server.getPageInfoByPageID(pageID);
				} catch (RemoteException e) {
					logger.info("Have an error when getting list of page info");
				}
			} catch (Exception e) {
				logger.info("Has problem when get pageID");
			}
			model.addAttribute("urlImage", pageInfo.getUrlImage());
			model.addAttribute("about", pageInfo.getAbout());
			model.addAttribute("description", pageInfo.getDescription());
			model.addAttribute("website", pageInfo.getWebsite());
			return "detailOfTopic";
		} catch (Exception ex){
			logger.error(ex.getMessage());
			return "error";
		}

	}
	
	/**
	 * Object to draw pie chart
	 * @param lisPieData
	 * @return
	 */
	public List<PieChart> getCharData(List<ListReportData> lstReportData){
		// loop all pieData input to calculate sum of each type color
		List<PieChart> lstPieChar = new ArrayList<PieChart>();
		
		int numOfPos = 0;
		int numOfNeg = 0;
		int numOfNeu = 0;
		for (ListReportData pieData : lstReportData) {
			switch (pieData.getSentimentType()) {
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
