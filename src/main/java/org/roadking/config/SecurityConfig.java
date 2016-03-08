package org.roadking.config;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService customUserDetailsService;

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		
		// Security configuration for H2 console access
		// !!!! You MUST NOT use this configuration for PRODUCTION site !!!!
		httpSecurity.authorizeRequests().antMatchers("/console/**").permitAll();
		httpSecurity.csrf().disable();
		httpSecurity.headers().frameOptions().disable();
		
		// static resources
		httpSecurity.authorizeRequests()
		.antMatchers("/css/**", "/js/**", "/images/**", "/resources/**", "/webjars/**").permitAll();
		
		httpSecurity.authorizeRequests()
						.antMatchers("/").permitAll()
						.antMatchers("/login").anonymous()
						.antMatchers("/userLoggin").permitAll()
						
						.antMatchers("/admin/**").hasRole("ADMIN")
						.antMatchers("/user/**").hasRole("USER")
						//.anyRequest().authenticated()
						.and()
					.formLogin()
						.loginPage("/login")
						.loginProcessingUrl("/login-process.html")
						.successHandler(saveRequestHandler())
						.failureUrl("/login?error")
						.usernameParameter("username")
						.passwordParameter("password")
						.defaultSuccessUrl("/", false)
						
						.and()
					.logout()
						.logoutSuccessUrl("/login?logout");
		
		httpSecurity.exceptionHandling().accessDeniedPage("/login");
		httpSecurity.sessionManagement().invalidSessionUrl("/login");		
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(customUserDetailsService);
		// In case of password encryption - for production site
		//auth.userDetailsService(customerUserDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public FilterRegistrationBean getSpringSecurityFilterChainBindedToError(
	                @Qualifier("springSecurityFilterChain") Filter springSecurityFilterChain) {
	        FilterRegistrationBean registration = new FilterRegistrationBean();
	        registration.setFilter(springSecurityFilterChain);
	        registration.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
	        return registration;
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
	@Bean
	public SavedRequestAwareAuthenticationSuccessHandler 
                saveRequestHandler() {
		
               SavedRequestAwareAuthenticationSuccessHandler auth 
                    = new SavedRequestAwareAuthenticationSuccessHandler();
       		auth.setUseReferer(true);
		return auth;
	}	
}
