package cn.bytecloud.steam.rule.dao;

import cn.bytecloud.steam.base.dao.BaseDao;
import cn.bytecloud.steam.base.dto.PageModel;
import cn.bytecloud.steam.constant.ModelConstant;
import cn.bytecloud.steam.rule.dto.RulePageDto;
import cn.bytecloud.steam.rule.entity.Rule;
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
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static cn.bytecloud.steam.constant.ModelConstant.*;

@Repository
public class RuleDaoImpl extends BaseDao<Rule> implements RuleDao {
    @Autowired
    private RuleRepository repository;
    @Autowired
    private MongoTemplate template;

    @Override
    public Rule save(Rule rule) {
        if (EmptyUtil.isEmpty(rule.getId())) {
            rule.setId(UUIDUtil.getUUID());
            rule.setCreateTime(System.currentTimeMillis());
            rule.setCreatorId(UserUtil.getUserId());
        } else {
            Rule old = repository.findOne(rule.getId());
            rule.setCreateTime(old.getCreateTime());
            rule.setCreatorId(old.getCreatorId());
        }
        rule.setUpdateTime(System.currentTimeMillis());
        repository.save(rule);
        return rule;
    }

    @Override
    public Rule findOne(final String id) {
        return repository.findOne(id);
    }

    @Override
    public void del(String id) {
        repository.delete(id);
    }

    @Override
    public PageModel list(RulePageDto dto) {
        List<AggregationOperation> list = addMatch(dto, CREATE_TIME);

        if (null != dto.getNumber()) {
            list.add(Aggregation.match(Criteria.where(RULE_NUMBER).is(dto.getNumber())));
        }
        if (EmptyUtil.isNotEmpty(dto.getTitle())) {
            list.add(Aggregation.match(Criteria.where(RULE_TITLE).regex(dto.getTitle())));
        }
        list.add(LookupOperation.newLookup()
                .from(T_USER).localField(CREATOR_ID)
                .foreignField(ID)
                .as("user"));
        if (EmptyUtil.isNotEmpty(dto.getCreator())) {
            list.add(Aggregation.match(Criteria.where("user." + USER_NAME).regex(dto.getCreator())));
        }
        list.add(Aggregation.sort(new Sort(Sort.Direction.ASC, RULE_NUMBER)));

        list.add(Aggregation.project()
                .and(RULE_TITLE).as("title")
                .and(RULE_NUMBER).as("number")
                .and("user." + USER_NAME).as("creator")
                .and(ID).as("id")
                .and(CREATE_TIME).as("createTime")
                .andExclude(ID)
        );

        return pageList(list, dto, T_RULE, CREATE_TIME);
    }

    @Override
    public List<Rule> homeList() {
        Query query = new Query();
        query.with(new Sort(Sort.Direction.ASC, ModelConstant.RULE_NUMBER));
        return template.find(query, Rule.class);
    }
}
