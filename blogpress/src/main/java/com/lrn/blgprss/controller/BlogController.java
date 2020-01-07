package com.lrn.blgprss.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
import com.lrn.blgprss.constants.CommentStatus;
import com.lrn.blgprss.model.Blog;
import com.lrn.blgprss.model.Comment;
import com.lrn.blgprss.service.BlogService;
import com.lrn.blgprss.util.BlogpressUtil;

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
	
	@GetMapping("/viewBlog")
	public String viewBlogPage(@RequestParam(value="blogId",required=true)String blogId,	Model model) {
		logger.info("displaying the view blog page");
		if(blogId != null) {
			Blog	blog	=	blogService.getBlog(blogId);
			List<Comment> approvedCommentList	=	null;
			
			if(blog.getComments()!=null && !blog.getComments().isEmpty()) {
				approvedCommentList	=	blog.getComments()
						.stream()
								.filter(comment->comment
												.getStatus()
													.equalsIgnoreCase("A"))
							.collect(Collectors.toList());
				if(approvedCommentList!=null) {
					blog.setComments(approvedCommentList);
				}else {
					blog.setComments(new ArrayList<Comment>());
				}
			}
			
			if (blog.getComments() == null || blog.getComments().isEmpty()) {
				blog.setComments(new ArrayList<Comment>());
			}
			model.addAttribute("blog",blog);
		}
		
		setProcessingData(model, BlogpressConstants.TITLE_VIEW_BLOG_PAGE);
		return "view-blog";
	}
	
	@GetMapping("/showComments")
	public String showManageComments() {
		logger.info("This si from showComments, for managing comments");
		return "manage-comments";
	}
	
	@PostMapping("/updateCommentStatus")
	public	String updateCommentStatus(@RequestParam(value="blogId",required=true)String blogId,
			@RequestParam(value="commentId",required=true)String commentId,
			@RequestParam(value="commentStatus",required=true)String commentStatus,
			Model model) {
		logger.info("Approve Comment");
		if(blogId!=null) {
			Blog	blog	=	blogService.getBlog(blogId);
			if(blog!=null) {
				blogService.updateCommentStatus(blogId,commentId,blog.getComments(),commentStatus);
			}
		}
		return "manage-comments";
	}
	
	@PostMapping("/addComment")
	public	String addComments(@RequestParam(value="blogId",required=true) String blogId,
			 @RequestParam(value = "name",required = true) String name, 
			 @RequestParam(value = "email",required = true) String email,
			 @RequestParam(value = "comment",required = true) String comment,
			 @RequestParam(value = "currentLevel",required = false,defaultValue="0") Integer currentLevel,
			 @RequestParam(value = "parentId",required = false,defaultValue="0") String parentId,
			 @RequestParam(value = "parentPosition",required = false) String parentPosition,
			 Model model) {
		logger.info("Add Comments Page");
		
		if(blogId!=null) {
			StringBuffer currentPositionStr	=	new	StringBuffer();
			int childSequence	=	blogService.getCurrentChildSeq(blogId,parentId);
		
			//HERE CREATING THE CURRENT POSITION WHICH IS A COMBINATON OF LEVEL AND CHILD SEQ
			if(parentPosition!=null) {
				currentPositionStr.append(parentPosition).append(".");
			}
			currentPositionStr.append(currentLevel+1).append(".").append(childSequence);
			
			//NOW CREATING THE BLOG
			Blog blog	=	blogService.getBlog(blogId);			
			
			if(blog!=null) {
				List<Comment> blogComments	=	blog.getComments();
				
				//IF THERE ARE NO BLOGS THEN INITIATE THE LIST OBJECT 
				if(blogComments	==	null) {
					blogComments	=	new	ArrayList<Comment>();
				}
				
				Date currentDate	=	new Date();
				Comment blogComment	=	new	Comment();
				blogComment.setId(BlogpressUtil.RandomNumber(currentDate));
				blogComment.setBlogId(blogId);
				blogComment.setParentId(parentId);
				blogComment.setChildSequence(childSequence);
				blogComment.setPosition(currentPositionStr.toString());
				blogComment.setStatus(CommentStatus.MODERATE.getStatus());
				blogComment.setLevel(currentLevel+1);
				blogComment.setUser(name);
				blogComment.setEmailAddress(email);
				blogComment.setCommentText(comment);
				blogComment.setCreatedDate(currentDate);
				
				blogComments.add(blogComment);
				blog.setComments(blogComments);
				blogService.addUpdateBlog(blog);
				model.addAttribute("blog",blog);
			}
		}
		
		setProcessingData(model, BlogpressConstants.TITLE_VIEW_BLOG_PAGE);
		return "redirect:viewBlog?blogId="+blogId;
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
