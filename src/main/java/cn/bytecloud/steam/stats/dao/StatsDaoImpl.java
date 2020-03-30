package cn.bytecloud.steam.stats.dao;

import cn.bytecloud.steam.base.dao.BaseDao;
import cn.bytecloud.steam.stats.dto.StatsDto;
import cn.bytecloud.steam.stats.entity.Stats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static cn.bytecloud.steam.constant.ModelConstant.*;

@Repository
public class StatsDaoImpl extends BaseDao<Stats> implements StatsDao {
    @Autowired
    private MongoTemplate template;

    @Autowired
    private StatsRepository repository;


    @Override
    public List<Stats> visitor(StatsDto dto) {
        Query query = new Query();
        Long startTime = dto.getStartTime();
        Long endTime = dto.getEndTime();
        if (startTime == null) {
            if (endTime != null) {
                query.addCriteria(Criteria.where(STATS_TIME).lte(endTime));
            }
        } else {
            if (endTime != null) {
                query.addCriteria(Criteria.where(STATS_TIME).gte(startTime).lte(endTime));
            } else {
                query.addCriteria(Criteria.where(STATS_TIME).gte(startTime));
            }
        }
        query.with(new Sort(Sort.Direction.ASC, STATS_TIME));
        List<Stats> list = template.find(query, Stats.class);
        return list;
    }
}
