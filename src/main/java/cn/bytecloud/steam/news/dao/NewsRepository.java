package cn.bytecloud.steam.news.dao;

import cn.bytecloud.steam.news.entity.News;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NewsRepository extends MongoRepository<News, String> {
    List<News> findAll(Sort sort);
}
