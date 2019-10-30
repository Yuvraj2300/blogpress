package com.lrn.blgprss.repo;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.lrn.blgprss.model.Blog;

@Repository
public interface BlogRepository extends ElasticsearchRepository<Blog, String> {

}
