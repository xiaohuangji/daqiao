package com.tg.admin.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tg.service.AdminService;

@Controller
@RequestMapping(value="/mobile")
public class SetAdminController {

	AdminService adminService;
	
	@RequestMapping(value="getmobile")
	public ModelAndView adminMobile(){
		ModelAndView mv = new ModelAndView("adminmobile");
		String mobile = adminService.getAdminMobile();
		mv.addObject("mobile", mobile);
		return mv;
	}
	
	@RequestMapping(value="setmobile",method=RequestMethod.POST)
	public ModelAndView setMobile(@RequestParam("mobile")String mobile){
		adminService.setAdminMobile(mobile);
		return adminMobile();
	}

	public AdminService getAdminService() {
		return adminService;
	}

	@Autowired
	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}
	
	
}
