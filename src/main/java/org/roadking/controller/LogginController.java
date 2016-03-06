package org.roadking.controller;


import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.roadking.service.CustomAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LogginController {

	private Logger log = Logger.getLogger(LogginController.class);
	
	@Autowired
	private CustomAuthService customAuthService;
	
	@RequestMapping(value = "/")
	public String indexPage() {
		return "index";
	}
	
	@RequestMapping(value = "/admin")
	public String adminPage() {
		//UserDetails userDetail = customUser.loadUserByUsername("admin");
		log.debug("ADMIN PAGE");
		
		return "admin";
	}
	
	@RequestMapping(value = "/user")
	public String userPage() {
		log.debug("USER PAGE");
		return "user";
	}

	@RequestMapping(value = "/userLoggin")
	public String userLogginPage( HttpServletRequest request, Model model) {
		if (request.getSession().getAttribute("SSO") == null) {
			log.info(SecurityContextHolder.getContext().getAuthentication().getName());
			UserDetails user = customAuthService.loadUserByUsername("admin");
			Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(auth);
			request.getSession().setAttribute("SSO", "ADMIN");
		}
		return "redirect:/";
	}
	
	@RequestMapping(value = "/login")
	public String loginSSOPage() {
		log.debug("Transfer login page");
		return "login";
	}
}
