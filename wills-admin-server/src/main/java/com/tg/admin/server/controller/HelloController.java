package com.tg.admin.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="/")
public class HelloController {

	@RequestMapping(value = "index", method = RequestMethod.GET)
	public ModelAndView hello() {
		ModelAndView mv = new ModelAndView("index");
		return mv ;
	}
}
