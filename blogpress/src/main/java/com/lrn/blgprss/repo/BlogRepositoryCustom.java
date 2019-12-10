package com.lrn.blgprss.repo;

import java.util.List;

import com.lrn.blgprss.model.Comment;

public interface BlogRepositoryCustom {
	public List<Comment> getAllComments(int from,int size);
}
