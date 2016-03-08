package org.roadking.controller;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LogginController {

	private Logger log = Logger.getLogger(LogginController.class);
	
	@Autowired
	@Qualifier(value="customAuthService")
	private UserDetailsService customAuthService;
	
	@Autowired
	private SavedRequestAwareAuthenticationSuccessHandler saveRequestHandler;
	
	
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
	public @ResponseBody String userLogginPage( HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("SSO") == null) {
			log.info(SecurityContextHolder.getContext().getAuthentication().getName());
			
			try {
				
				UserDetails user = customAuthService.loadUserByUsername("admin");
				if(user != null){
					log.info("Login Success");
					Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(auth);
					request.getSession().setAttribute("SSO", "ADMIN");
					saveRequestHandler.onAuthenticationSuccess(request, response, auth);
				}
				else{
					log.warn("Login fail");
					//Manually Login 실패시 처리 로직
					//request.getRequestDispatcher("/").forward(request, response);
				}
				
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "redirect:/";
	}
	
	@RequestMapping(value = "/login")
	public String loginSSOPage() {
		log.debug("Transfer login page");
		return "login";
	}
}
