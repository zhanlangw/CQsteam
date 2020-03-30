package cn.bytecloud.steam.stats.dao;

import cn.bytecloud.steam.stats.entity.Stats;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatsRepository extends MongoRepository<Stats, String> {
    List<Stats> findByUsernameAndBidAndTime(String username, String bid, long time);
}
