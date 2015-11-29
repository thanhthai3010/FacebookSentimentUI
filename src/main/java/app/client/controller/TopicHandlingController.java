package app.client.controller;

import java.rmi.RemoteException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import app.utils.dto.ListTopic;

@Controller
public class TopicHandlingController {

	private static final Logger logger = LoggerFactory
			.getLogger(TopicHandlingController.class);

	private ListTopic describeTopics;
	
	@RequestMapping(value = "/a", method = RequestMethod.GET)
	public String indexPage(Model model) {
		return "index";
	}
	
	@RequestMapping(value = "/processLDA", method = RequestMethod.POST)
	public String listTopicsView(Model model) {
		return "viewTopics";

	}

	@ResponseBody
	@RequestMapping(value = "/getListTopic", method = RequestMethod.GET, produces = "text/html; charset=utf-8") 
	public String getListTopicsView(Model model, HttpServletRequest req) throws RemoteException {
		
		logger.info("Read from Server");
		this.describeTopics = HomeController.server.getDescribleTopics();
		
		return describeTopics.toTopicsJson();
	}

	public void drawPieChart() {
		
	}
}
