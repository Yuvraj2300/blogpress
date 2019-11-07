package com.lrn.blgprss.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lrn.blgprss.constants.BlogStatus;
import com.lrn.blgprss.constants.BlogpressConstants;
import com.lrn.blgprss.model.Blog;
import com.lrn.blgprss.service.BlogService;

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

	@Autowired
	BlogService blogService;

	@GetMapping("/")
	public String showLandingPage(Model model) {
		logger.info("This is from show landing page method");
		setProcessingData(model, BlogpressConstants.TITLE_HOME_PAGE);
		return "home";
	}

	@GetMapping("/controlPage")
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
	 * @param model accepts a model
	 * @return the template from adding a new blog
	 */
	@GetMapping("/showAddNew")
	public String showAddNew(Model model) {
		logger.info("Showing add new template");
		setProcessingData(model, BlogpressConstants.TITLE_NEW_BLOG_PAGE);
		return "add-new";
	}

	@GetMapping("/addNewBlog")
	public String addNewBlog(@RequestParam(required = true, value = "title") String title,
			@RequestParam(required = true, value = "body") String body, Model model) {
		
		logger.info("adding a blog with title: "+title);
		Blog blog = new Blog();
		blog.setBody(body);
		blog.setTitle(title);
		blog.setCreatedBy(getCurrentUserName());
		blog.setPublishDate(new Date());
		blog.setStatus(BlogStatus.PUBLISHED.getStatus());

		blogService.addUpdateBlog(blog);

		return "home";
	}

	@ModelAttribute("currentUserName")
	private String getCurrentUserName() {
		// String username =
		// SecurityContextHolder.getContext().getAuthentication().getName();
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	/**
	 * Method checks if user is logged in
	 * 
	 * @return a boolean if a user is logged in
	 */
	@ModelAttribute("validUserLogin")
	public boolean isUserLoggedIn() {
		return SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
				// when Anonymous Authentication is enabled
				!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
	}

	@ModelAttribute("hasAdminRole")
	public boolean checkIfUserHasAdminRole() {
		return checkIfUserHasRole(BlogpressConstants.ROLE_ADMIN);
	}

	@ModelAttribute("hasUserRole")
	public boolean checkIfUserHasRole() {
		return checkIfUserHasRole(BlogpressConstants.ROLE_USER);
	}

	/**
	 * THIS METHOD CHECKS THE ROLE/AUTHORITIES OF ALL THE USERS AND MATCHES WITH THE
	 * PASSED USER ROLE.
	 * 
	 * @param roleName
	 * @return TRUE/FALSE
	 */
	private boolean checkIfUserHasRole(String roleName) {
		boolean hasUserRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.anyMatch(role -> role.getAuthority().equals(roleName));

		return hasUserRole;
	}

	/**
	 * This method stores various data which are required on presentation layer.
	 * 
	 * @param model
	 * @param pageTitle
	 */
	private void setProcessingData(Model model, String pageTitle) {
		model.addAttribute(BlogpressConstants.PAGE_TITLE, pageTitle);
	}

}
