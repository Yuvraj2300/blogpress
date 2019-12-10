package com.lrn.blgprss.repo.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lrn.blgprss.model.Blog;
import com.lrn.blgprss.model.Comment;
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


	/**
	 * @param 	response String/Response String to be passed into the method
	 * @return 	A list of type Blog, consisting all the blogs that were obtained 
	 * 			in the response 
	 */
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


	@Override
	public List<Comment> getAllComments(int from, int size) {
		NestedAggregationBuilder	aggregation	=	AggregationBuilders.nested("aggChild", "comments")
				.subAggregation(AggregationBuilders.topHits("aggSortComment")
						.sort("comments.createdDate",SortOrder.DESC).from(from).size(size));
		
		
		System.out.println("This is the aggregation query for "
				+ "for getting the comment: "+aggregation.toString());
		
		SearchResponse	response	=	esTemp.getClient().prepareSearch("blog")
											.setTypes("blog").addAggregation(aggregation)
												.execute().actionGet();
		List<Aggregation> responseAgg	=	response.getAggregations().asList();
		
		return getAllCommentsFromJSON(responseAgg.get(0).toString());
	}

	
	public List<Comment>	getAllCommentsFromJSON(String response){
		List<Comment> commentsList	=	new ArrayList<>();
		ObjectMapper	mapper	=	new	 ObjectMapper();
		
		if (response != null) {
			JSONObject searchJson = new JSONObject(response);
			if (searchJson.get("aggChild") != null) {
				JSONObject aggChildObj = searchJson.getJSONObject("aggChild");
				if (aggChildObj != null && aggChildObj.getJSONObject("aggSortComment") != null) {
					JSONObject aggSortCommentObj = aggChildObj.getJSONObject("aggSortComment");
					if (aggSortCommentObj != null && aggSortCommentObj.getJSONObject("hits") != null) {
						JSONObject hitObj = aggSortCommentObj.getJSONObject("hits");
						try {
							if (hitObj != null && hitObj.getJSONArray("hits") != null) {
								JSONArray hitsArray = hitObj.getJSONArray("hits");

									for(int i=0;i<hitsArray.length();i++) {
										commentsList.add(mapper.readValue(
												hitsArray.getJSONObject(i)
												.getJSONObject("_source").toString(), Comment.class));
									}
							}
						} catch (JSONException | IOException e) {
							logger.error("There was an error parsing the resposnse JSON.");
						}
					}

				}
			}
		}
		
		return commentsList;
	}


	@Override
	public int getCurrentChildSeq(String blogId, String parentId) {
		int currentChildSeq	=	0;
		TermQueryBuilder termQueryBuilder	=	
				new	 TermQueryBuilder("commnet.parentId",parentId);
		
		NestedAggregationBuilder	aggregationBuilder	=
				AggregationBuilders.nested("aggChild", "comments")
					.subAggregation(
							AggregationBuilders.filter("filterParentid", termQueryBuilder)
							.subAggregation(
									AggregationBuilders.max("maxChildSeq")
										.field("comments.childSequence")));
		
		TermQueryBuilder	rootTermQueryBuilder	=
				new	TermQueryBuilder("_id",blogId);
		
		SearchResponse response	=	esTemp.getClient().prepareSearch("blog")
				.setTypes("blog")
				.setQuery(rootTermQueryBuilder)
				.addAggregation(aggregationBuilder)
				.execute().actionGet();
		
		
		if(response!=null) {
			if(response.getAggregations()!=null) {
				List<Aggregation> aggList	=	response.getAggregations().asList();
				if(aggList!=null) {
					Aggregation resultAgg	=	aggList.get(0);
					if(resultAgg!=null) {
						currentChildSeq	=	getMaxChildSequenceFromJson(resultAgg.toString());
					}
				}
			}
		}
		
		currentChildSeq++;
		
		return currentChildSeq;
	}


	private int getMaxChildSequenceFromJson(String aggJson) {
		int childSequence	=	0;
		double maxChildSeq	=	0.0;
		
		if(aggJson!=null) {
			JSONObject	commentJson	=	new JSONObject(aggJson);
			if(commentJson.get("aggChild")!=null) {
				
			}
		}
		return 0;
	}
	
}
