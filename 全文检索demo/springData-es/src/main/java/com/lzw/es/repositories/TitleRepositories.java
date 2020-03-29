package com.lzw.es.repositories;


import com.lzw.es.pojo.Title;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TitleRepositories extends ElasticsearchRepository<Title, Long> {
}
