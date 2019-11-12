package com.lrn.blgprss.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

	
	public List<Blog> getAllBlogs() {
		List<Blog> blogList = new ArrayList<Blog>();
		Iterable<Blog> blogIterable = blogRepo.findAll();
		Iterator<Blog> blogIterator = blogIterable.iterator();

		while (blogIterator.hasNext()) {
			blogList.add(blogIterator.next());
		}

		return blogList;
	}
}
