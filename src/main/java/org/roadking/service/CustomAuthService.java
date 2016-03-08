/**
 * 
 */
package org.roadking.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.roadking.model.UserRole;
import org.roadking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Roadking
 *
 */
@Service
public class CustomAuthService implements UserDetailsService{
	private Logger log = Logger.getLogger(CustomAuthService.class);
	
	@Autowired
	private UserRepository userRepository;

	@Transactional
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		org.roadking.model.Users user =  userRepository.findByUsername(username);
		List<GrantedAuthority> authorities = buildUserAuthority(user.getUserRoles());
		log.debug("CUSTOM Authority created");
		return buildUserForAuthentication(user,authorities);
	}
	
	private User buildUserForAuthentication(org.roadking.model.Users user,
			List<GrantedAuthority> authorities) {
		
		return new User(user.getUsername(),user.getPassword(),true,true,true,true,authorities);
		
	}
	private List<GrantedAuthority> buildUserAuthority(Set<UserRole> userRoles) {
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(0);
		for (UserRole userRole : userRoles) {
			authorities.add(new SimpleGrantedAuthority(userRole.getUserRoleName()));
		}
		return authorities;
	}
	
}
