package com.lrn.blgprss.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lrn.blgprss.model.Blog;
import com.lrn.blgprss.model.Comment;
import com.lrn.blgprss.repo.BlogRepository;

@Service
public class BlogService {
	@Autowired
	private BlogRepository blogRepo;

	public void addUpdateBlog(Blog blog) {
		blogRepo.save(blog);
	}

	
	public Blog getBlog(String id) {
		Optional<Blog> retBlog = blogRepo.findById(id);
		if (retBlog.isPresent()) {
			return retBlog.get();
		} else {
			return null;
		}
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
	
	public List<Comment> getAllComments(int from,int size){
		return blogRepo.getAllComments(from, size);
	}
	
	public int getCurrentChildSeq(String blogId, String parentId) {
		return blogRepo.getCurrentChildSeq(blogId,parentId);
	}
	
}
