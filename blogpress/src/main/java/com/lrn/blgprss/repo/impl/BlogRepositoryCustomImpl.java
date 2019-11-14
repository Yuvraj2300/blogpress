package com.lrn.blgprss.repo.impl;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Repository;

import com.lrn.blgprss.repo.BlogRepositoryCustom;

@Repository
public class BlogRepositoryCustomImpl implements BlogRepositoryCustom{
	private	Logger	logger	=	Logger.getLogger(BlogRepositoryCustomImpl.class);
	
	@Autowired
	private	ElasticsearchTemplate esTemp;
}
