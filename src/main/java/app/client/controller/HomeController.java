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
import app.utils.dto.Page_Info;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory
			.getLogger(HomeController.class);

	public static ServerInterf server;
	
	private Registry myRegis;
	
	/**
	 * View Home page
	 * @param model
	 * @return home page of application
	 */
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
				logger.info("finded server: " + server.hello());
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
		return "homePage";
	}

	/**
	 * View page for get facebook data
	 * @param model
	 * @param rep
	 * @return
	 */
	@RequestMapping(value = "/getFBData", method = RequestMethod.GET)
	public String viewGetFBDataPage(Model model, HttpServletRequest rep){
		logger.info("Welcome get fb data page!");
		return "getFBData";
	}
	
	/**
	 * View main page, analysis facebook data
	 * @param model
	 * @param rep
	 * @return
	 */
	@RequestMapping(value = "/analysisData", method = RequestMethod.GET)
	public String viewAnalysisDataPage(Model model, HttpServletRequest rep){
		logger.info("Welcome analyzing page!");
		
		List<Page_Info> listPageInfo = new ArrayList<Page_Info>();
		try {
			listPageInfo = server.getListPageInfo();
		} catch (RemoteException e) {
			logger.info("Have an error when getting list of page info");
		}
		
		model.addAttribute("listPageInfo", listPageInfo);
		
		return "analysisData";
	}
	
	/**
	 * View about page
	 * @param model
	 * @param rep
	 * @return
	 */
	@RequestMapping(value = "/aboutUs", method = RequestMethod.GET)
	public String viewAboutUsPage(Model model, HttpServletRequest rep){
		logger.info("Welcome about us page!");
		return "aboutUs";
	}
}
