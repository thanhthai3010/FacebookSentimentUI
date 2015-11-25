package spring.fb.demo;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import app.server.handling.ServerInterf;
import app.utils.dto.ListTopic;

@Controller
public class TopicHandlingController {

	private static final Logger logger = LoggerFactory
			.getLogger(TopicHandlingController.class);
	
	private ServerInterf server;
	private Registry myRegis;
	
	@RequestMapping(value = "/viewTopic", method = RequestMethod.GET)
	public String listTopicsView(Model model) {
		return "viewTopics";

	}

	@ResponseBody
	@RequestMapping(value = "/getListTopic", method = RequestMethod.GET, produces = "text/html; charset=utf-8") 
	public String getListTopicsView(Model model) throws RemoteException {
		
		try {
			if (myRegis == null) {
				myRegis = LocateRegistry.getRegistry("127.0.0.1");
			}
			if (server == null) {
				server = (ServerInterf) myRegis.lookup("server");
			}
			if (server != null) {
				System.out.println("qtran: " + server.hello());
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
		
		ListTopic describeTopics = server.getDescribeTopics();
		
		return describeTopics.toTopicsJson();
	}

	public void drawPieChart() {
		
	}
}
