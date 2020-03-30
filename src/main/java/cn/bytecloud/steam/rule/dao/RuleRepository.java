package cn.bytecloud.steam.rule.dao;

import cn.bytecloud.steam.rule.entity.Rule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleRepository extends MongoRepository<Rule, String> {
}
