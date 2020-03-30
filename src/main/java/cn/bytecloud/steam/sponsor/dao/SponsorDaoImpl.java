package cn.bytecloud.steam.sponsor.dao;

import cn.bytecloud.steam.base.dao.BaseDao;
import cn.bytecloud.steam.sponsor.dto.SponsorPageDto;
import cn.bytecloud.steam.sponsor.entity.Sponsor;
import cn.bytecloud.steam.util.EmptyUtil;
import cn.bytecloud.steam.util.UUIDUtil;
import cn.bytecloud.steam.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static cn.bytecloud.steam.constant.ModelConstant.*;

@Repository
public class SponsorDaoImpl extends BaseDao<Sponsor> implements SponsorDao {
    @Autowired
    private MongoTemplate template;
    @Autowired
    private SponsorRepository repository;

    @Override
    public Sponsor save(Sponsor video) {
        if (EmptyUtil.isEmpty(video.getId())) {
            video.setId(UUIDUtil.getUUID());
            video.setCreatorId(UserUtil.getUserId());
            video.setCreateTime(System.currentTimeMillis());
        } else {
            Sponsor old = repository.findOne(video.getId());
            video.setCreateTime(old.getCreateTime());
            video.setCreatorId(old.getCreatorId());
        }
        video.setUpdateTime(System.currentTimeMillis());
        repository.save(video);
        return video;

    }

    @Override
    public Object list(SponsorPageDto dto) {
        List<AggregationOperation> list = addMatch(dto, UPDATE_TIME);

        if (EmptyUtil.isNotEmpty(dto.getUrl())) {
            list.add(Aggregation.match(Criteria.where(SPONSOR_url).regex(dto.getUrl())));
        }

        list.add(LookupOperation.newLookup()
                .from(T_USER)
                .localField(CREATOR_ID)
                .foreignField(ID)
                .as("user")
        );
        if (EmptyUtil.isNotEmpty(dto.getCreator())) {
            list.add(Aggregation.match(Criteria.where("user." + USER_NAME).regex(dto.getCreator())));
        }
        list.add(Aggregation.sort(new Sort(Sort.Direction.DESC, UPDATE_TIME)));

        list.add(Aggregation.project()
                .and(SPONSOR_url).as("url")
                .and("user." + USER_NAME).as("creator")
                .and(UPDATE_TIME).as("updateTime")
                .and(SPONSOR_PATH).as("path")
                .and(ID).as("id")
                .andExclude(ID)
        );

        return pageList(list, dto, T_SPONSOR, UPDATE_TIME);
    }
}
