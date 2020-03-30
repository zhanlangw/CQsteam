package cn.bytecloud.steam.result.dao;

import cn.bytecloud.steam.base.dao.BaseDao;
import cn.bytecloud.steam.base.dto.PageModel;
import cn.bytecloud.steam.result.dto.ResultPageDto;
import cn.bytecloud.steam.result.entity.Result;
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
import java.util.HashMap;
import java.util.List;

import static cn.bytecloud.steam.constant.ModelConstant.*;

@Repository
public class ResultDaoImpl extends BaseDao<Result> implements ResultDao {
    @Autowired
    private MongoTemplate template;

    @Autowired
    private ResultRepository repository;

    @Override
    public Result save(Result result) {
        if (EmptyUtil.isEmpty(result.getId())) {
            result.setCreateTime(System.currentTimeMillis());
            result.setCreatorId(UserUtil.getUserId());
            result.setId(UUIDUtil.getUUID());
        } else {
            Result old = repository.findOne(result.getId());
            result.setCreateTime(old.getCreateTime());
            result.setCreatorId(old.getCreatorId());
        }
        result.setUpdateTime(System.currentTimeMillis());
        repository.save(result);
        return result;
    }

    @Override
    public PageModel<HashMap> list(ResultPageDto dto) {
        List<AggregationOperation> list = addMatch(dto, CREATE_TIME);

        if (EmptyUtil.isNotEmpty(dto.getTitle())) {
            list.add(Aggregation.match(Criteria.where(RESULT_TITLE).regex(dto.getTitle())));
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
        list.add(Aggregation.sort(new Sort(Sort.Direction.DESC, CREATE_TIME)));

        list.add(Aggregation.project()
                .and(RESULT_TITLE).as("title")
                .and(RESULT_PATH).as("path")
                .and("user." + USER_NAME).as("creator")
                .and(CREATE_TIME).as("createTime")
                .and(ID).as("id")
                .andExclude(ID)
        );

        return pageList(list, dto, T_RESULT, CREATE_TIME);
    }
}
