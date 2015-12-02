package app.client.controller;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import app.server.handling.ServerInterf;
import app.utils.dto.FacebookData;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory
			.getLogger(HomeController.class);

	public static ServerInterf server;
	private Registry myRegis;

	// THAINT
	private FacebookData inputDataForService;

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
				logger.info("qtran: " + server.hello());
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
		logger.info("View word cloud of topic");

		List<String> lstPageID = new ArrayList<String>();
		lstPageID.add("447498478655695");
		

		//TODO
		// please check value of getFBDataByPageIDAndDate() method return.
		// If we does not have data, show message for user.
		this.inputDataForService = new FacebookData();
		inputDataForService = HomeController.server.getFBDataByPageIDAndDate(
				lstPageID, "2015-11-22", "2015-11-30");
		
		server.processLDA(inputDataForService);

		return "viewTopics";
	}
	
	@RequestMapping(value = "/sumitPageID", method = RequestMethod.POST)
	public String processLDA(Model model, HttpServletRequest req){
		
		return null;
	}
}
