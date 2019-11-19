package com.lrn.blgprss.repo.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lrn.blgprss.model.Blog;
import com.lrn.blgprss.repo.BlogRepositoryCustom;

@Repository
public class BlogRepositoryCustomImpl implements BlogRepositoryCustom{
	private	Logger	logger	=	Logger.getLogger(BlogRepositoryCustomImpl.class);
	
	@Autowired
	private	ElasticsearchTemplate esTemp;
	
	
	/**
	 * @param searchText
	 * @return Returns a list containing the blogs found by the query
	 */
	public List<Blog> search(String searchText){
		
		QueryBuilder booleanQry	=	QueryBuilders.boolQuery()
				.should(QueryBuilders.termQuery("title", searchText))
				.should(QueryBuilders.termQuery("body", searchText));
		
		SearchResponse response	= esTemp.getClient().prepareSearch("blog")
				.setTypes("blog")
				.setQuery(booleanQry)
				.execute().actionGet();
		
		List<Blog> blogSearchResult	=	getBlogListFromSearchJSON(response.toString());
		
		return	null;
	}


	private List<Blog> getBlogListFromSearchJSON(String response) {
		// TODO Auto-generated method stub
		List<Blog> blogsList	=	new	 ArrayList<Blog>();
		ObjectMapper	objMapper	=	new ObjectMapper();
		if(response!=null) {
			JSONObject	searchJson	=	new	JSONObject(response);
			if(searchJson.get("hits")!=null) {
				JSONObject resultJson	=	searchJson.getJSONObject("hits");
				try {	
					if(resultJson!=null || resultJson.get("hits")!=null) {
						JSONArray blogArray = resultJson.getJSONArray("hits");

						for(int i=0;i<blogArray.length();i++) {
							blogsList.add(objMapper.readValue(blogArray.getJSONObject(i)
									.getJSONObject("_source").toString()
									, Blog.class));
						}
					}
				}catch(JSONException | IOException e) {
					logger.error("error occured while fetching all the comments"+ e.getMessage(),e);
				}
			}
		}
		return blogsList;
	}

}
