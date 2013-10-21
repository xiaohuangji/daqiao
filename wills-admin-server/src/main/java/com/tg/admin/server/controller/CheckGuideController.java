package com.tg.admin.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tg.model.GuideInfo;
import com.tg.model.UserInfo;
import com.tg.service.AdminService;

@Controller
@RequestMapping(value="/check")
public class CheckGuideController {
	
	private AdminService adminService;

	@RequestMapping(value="getapply")
	public ModelAndView getAllApplyForGuideUsers(){
		ModelAndView mv = new ModelAndView("guides");
		List<GuideInfo> users = adminService.getAllApplyForGuideUsersExt();
		mv.addObject("users", users);
		return mv;
	}
	
	@RequestMapping(value="tobeguide")
	public ModelAndView toBeGuide(@RequestParam(value = "userId")int userId){
		adminService.toBeGuide(userId);
		return getAllApplyForGuideUsers();
	}
	
	@RequestMapping(value="getallguide")
	public ModelAndView getAllGuide(@RequestParam(value = "pageNo")int pageNo){
		int rows=10;
		ModelAndView mv = new ModelAndView("allguides");
		List<UserInfo> users= adminService.getAllGuide((pageNo-1)*rows, rows);
		mv.addObject("users", users);
		mv.addObject("pageNo",pageNo);
		mv.addObject("hasNext",((users!=null&&users.size()==rows)?true:false));
		return mv;
	}

	@Autowired
	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}
	
}
