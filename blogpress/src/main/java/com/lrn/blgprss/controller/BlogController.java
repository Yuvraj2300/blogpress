package com.lrn.blgprss.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.lrn.blgprss.constants.BlogpressConstants;

/**
 * @author ys19299
 *
 */
/**
 * @author ys19299
 *
 */
@Controller
public class BlogController {
	private Logger logger = LoggerFactory.getLogger(BlogController.class);

	@GetMapping("/")
	public String showLandingPage(Model model) {
		logger.info("This is from show landing page method");
		setProcessingData(model, BlogpressConstants.TITLE_HOME_PAGE);
		return "home";
	}

	@GetMapping("/contolPage")
	public String showControlPage(Model model) {
		logger.info("This is from control page method");
		setProcessingData(model, BlogpressConstants.TITLE_LANDING_CONTROL_PAGE);
		return "control-page";

	}

	@GetMapping("/login")
	public String showLoginPage(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, Model model) {
		logger.info("This is from login page method");

		if (error != null) {
			model.addAttribute("error", "Invalid Credentials Provided.");
		}

		if (logout != null) {
			model.addAttribute("message", "Logged Out");
		}

		setProcessingData(model, BlogpressConstants.TITLE_LOGIN_PAGE);

		return "login";
	}

	
	/**
	 * Method checks if user is logged in
	 * @return a boolean if a user is logged in
	 */
	@ModelAttribute("validUserLogin")
	public	boolean isUserLoggedIn() {
		return SecurityContextHolder.getContext().getAuthentication() != null && 
				SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
				 //when Anonymous Authentication is enabled
				 !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken); 
	}
	
	@ModelAttribute("hasAdminRole")
	public boolean checkIfUserHasAdminRole() {
		return  checkIfUserHasRole(BlogpressConstants.ROLE_ADMIN);
	}
	
	@ModelAttribute("hasUserRole")
	public	boolean checkIfUserHasRole() {
		return checkIfUserHasRole(BlogpressConstants.ROLE_USER);
	}
	
	private	boolean checkIfUserHasRole(String roleName) {
		boolean hasUserRole	=	SecurityContextHolder.getContext()
				.getAuthentication().getAuthorities().stream()
					.anyMatch(role->role.getAuthority().equals(roleName));
		
		return hasUserRole;
	}
	
	/**
	 * This method stores various data which are required on presentation layer.
	 * @param model
	 * @param pageTitle
	 */
	private void setProcessingData(Model model,String pageTitle) {
		model.addAttribute(BlogpressConstants.PAGE_TITLE, pageTitle);
	}

}
