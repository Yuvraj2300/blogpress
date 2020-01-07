package com.lrn.blgprss.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lrn.blgprss.model.Blog;
import com.lrn.blgprss.model.Comment;
import com.lrn.blgprss.service.BlogService;

@RestController
@RequestMapping("/api")
public class BlogRESTController {
	private Logger logger = LoggerFactory.getLogger(BlogRESTController.class);

	@Autowired
	private BlogService blogService;
	
	@GetMapping(value="/listBlogs",produces=MediaType.APPLICATION_JSON_VALUE)
	public	ResponseEntity<List<Blog>> getAllBlogJSON(){
		logger.info("getting all the blogs in JSON format");
		List<Blog> allBlogs	=	blogService.getAllBlogs();
		//SKIPPING THE COMMENTS LOGIC FOR NOW
		return new ResponseEntity<List<Blog>>(allBlogs,HttpStatus.OK);
	}
	
	@GetMapping(value="/listAllComments",produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Comment>> getAllComments(){
		logger.info("getting all the cmments for blogs");
		List<Comment> allComments	=	blogService.getAllComments(0,100);
		
		return new	 ResponseEntity<List<Comment>>(allComments,HttpStatus.OK);
	}
	
}
