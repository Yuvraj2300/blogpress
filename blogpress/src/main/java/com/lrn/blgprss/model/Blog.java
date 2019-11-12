package com.lrn.blgprss.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(indexName = "blog", type = "blog")
public class Blog {

	/*
	 * private String _id;
	 * 
	 * Commenting this cuz of: -
	 * org.elasticsearch.index.mapper.MapperParsingException: Field [_id] is a
	 * metadata field and cannot be added inside a document. Use the index API
	 * request parameters.
	 * 
	 * Following suggstions from
	 * https://stackoverflow.com/questions/40190288/use-of-id-for-document-
	 * identifier-in-elastic-search
	 * 
	 */
	@Id
	private String id;
	private String title;
	private String body;
	private String status;
	private String createdBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy'T'HH:mm:ss")
	private Date createdDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy'T'HH:mm:ss")
	private Date publishDate;

	@Field(includeInParent = true, type = FieldType.Nested)
	private List<Comment> comments;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

}
