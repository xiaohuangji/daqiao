package com.renren.intl.soundsns.controllers;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.renren.intl.soundsns.utils.HeartBeatService;

/**
 * Handles requests for the application home page.
 */
@Controller 
public class ServiceActivationController {
	
	
	

	private HeartBeatService heartBeatService;
	 
	@Autowired
	public void setHeartBeatService(HeartBeatService heartBeatService) {
		this.heartBeatService = heartBeatService;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listAllBoard(Model model) throws UnknownHostException { 
		 
		model.addAttribute("allServers",heartBeatService.getAllServers());
		model.addAttribute("thisServer",heartBeatService.getNodeName());
		model.addAttribute("status", heartBeatService.getServerStatus());
		
		return "home";
    }
	
	@RequestMapping(value = "/enable", method = RequestMethod.GET)
    public String enableService(Model model,HttpServletRequest request) throws UnknownHostException, KeeperException, InterruptedException { 
		heartBeatService.enableServer(); 
		model.addAttribute("server",heartBeatService.getNodeName());
		model.addAttribute("status", heartBeatService.getServerStatus());
		
		return "redirect:/list";
    }
	
	@RequestMapping(value = "/disable", method = RequestMethod.GET)
    public String disableService(Model model) throws UnknownHostException, KeeperException, InterruptedException { 
		
		heartBeatService.disableServer();
		String ip =   InetAddress.getLocalHost().getHostAddress();
		model.addAttribute("server",ip);
		model.addAttribute("status", heartBeatService.getServerStatus());
		
		return "redirect:/list";
    }
	
}
