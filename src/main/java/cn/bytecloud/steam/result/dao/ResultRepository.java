package cn.bytecloud.steam.result.dao;

import cn.bytecloud.steam.result.entity.Result;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends MongoRepository<Result, String> {
}
