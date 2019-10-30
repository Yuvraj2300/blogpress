package com.lrn.blgprss.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lrn.blgprss.model.Blog;
import com.lrn.blgprss.repo.BlogRepository;

@Service
public class BlogService {
	@Autowired
	private BlogRepository blogRepo;

	public void addUpdateBlog(Blog blog) {
		blogRepo.save(blog);
	}
}
