package org.roadking.service;

import org.apache.log4j.Logger;
import org.roadking.model.Users;
import org.roadking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	private Logger log = Logger.getLogger(UserService.class);
	
	@Autowired
	private UserRepository userRepository;
	
	
	public void printUsers(){
		Users user =  userRepository.findByUsername("admin");
		log.info("Username : "+user.getUsername());
	}
	

}
