package cn.bytecloud.steam.user.dao;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.exception.ErrorCode;
import cn.bytecloud.steam.base.dao.BaseDao;
import cn.bytecloud.steam.constant.ModelConstant;
import cn.bytecloud.steam.stats.dto.StatsDto;
import cn.bytecloud.steam.user.dto.PageUserDto;
import cn.bytecloud.steam.user.entity.User;
import cn.bytecloud.steam.user.entity.UserType;
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
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static cn.bytecloud.steam.constant.ModelConstant.*;

@Repository
public class UserDaoImpl extends BaseDao<User> implements UserDao {
    @Autowired
    private MongoTemplate template;

    @Autowired
    private UserRepository repository;

    @Override
    public List<User> findByName(final String asdf) {
        return repository.findByName(asdf);
    }

    @Override
    public User findOne(final String id) {
        return repository.findOne(id);
    }

    @Override
    public User save(User user) throws ByteException {
        if (EmptyUtil.isEmpty(user.getId())) {
            user.setId(UUIDUtil.getUUID());
            user.setCreateTime(System.currentTimeMillis());
            if (user.getType() != UserType.USER) {
                user.setCreatorId(UserUtil.getUserId());
            }
        } else {
            User old = repository.findOne(user.getId());
            if (old.getType() == UserType.ROOT) {
                throw new ByteException(ErrorCode.FAILURE, "禁止修改");
            }
            user.setUserflag(old.isUserflag());
            user.setType(old.getType());
            user.setCreateTime(old.getCreateTime());

            if (old.getType() == user.getType()) {
                user.setType(old.getType());
            }
            user.setCreatorId(UserUtil.getUserId());
        }
        user.setUpdateTime(System.currentTimeMillis());
        repository.save(user);
        return user;
    }

    @Override
    public void updPassword(String telephone, String password) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.USER_USERNAME).is(telephone));
        Update update = new Update();
        update.set(ModelConstant.USER_PASSWORD, password);
        template.updateFirst(query, update, User.class);
    }

    @Override
    public List<User> findByRoleId(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.USER_ROLE_IDS).elemMatch(new Criteria().in(id)));
        return template.find(query, User.class);
    }

    @Override
    public void resetPasswrod(String id, String password) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.ID).is(id));
        Update update = new Update();
        update.set(ModelConstant.USER_PASSWORD, password);
        template.updateFirst(query, update, User.class);
    }

    @Override
    public Object list(PageUserDto dto, boolean flag) {
        List<AggregationOperation> list = addMatch(dto, CREATE_TIME);
        if (EmptyUtil.isNotEmpty(dto.getName())) {
            list.add(Aggregation.match(Criteria.where(USER_USERNAME).regex(dto.getName())));
        }
        if (flag) {
            list.add(Aggregation.match(Criteria.where(USER_TYPE).is(UserType.USER.name())));
            list.add(Aggregation.sort(new Sort(Sort.Direction.DESC, CREATE_TIME)));
            list.add(Aggregation.project()
                    .and(USER_USERNAME).as("name")
                    .and(CREATE_TIME).as("createTime")
                    .and(ID).as("id")
                    .and(USER_USER_FLAG).as("userFlag")
                    .andExclude(ID)
            );
        } else {
            list.add(Aggregation.match(Criteria.where(USER_TYPE).ne(UserType.USER.name())));
            list.add(Aggregation.sort(new Sort(Sort.Direction.DESC, CREATE_TIME)));
            list.add(LookupOperation.newLookup()
                    .from(T_USER)
                    .localField(CREATOR_ID)
                    .foreignField(ID)
                    .as("user")
            );
            if (EmptyUtil.isNotEmpty(dto.getCreator())) {
                list.add(Aggregation.match(Criteria.where("user." + USER_NAME).regex(dto.getCreator())));
            }

            list.add(Aggregation.project()
                    .and(USER_NAME).as("name")
                    .and("user." + USER_NAME).as("creator")
                    .and(CREATE_TIME).as("createTime")
                    .and(ID).as("id")
                    .andExclude(ID)
            );
        }

        return pageList(list, dto, T_USER, CREATE_TIME);
    }

    @Override
    public List<User> findByData(Date date) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.CREATE_TIME).gte(date.getTime()));
        query.addCriteria(Criteria.where(USER_TYPE).is(UserType.USER));
        return template.find(query, User.class);
    }

    @Override
    public List<HashMap> registerStats(StatsDto dto) {
        Long startTime = dto.getStartTime();
        Long endTime = dto.getEndTime();
        List<AggregationOperation> list = new ArrayList<>();
        if (startTime == null) {
            if (endTime != null) {
                list.add(Aggregation.match(Criteria.where(CREATE_TIME).lte(endTime)));
            }
        } else {
            if (endTime != null) {
                list.add(Aggregation.match(Criteria.where(CREATE_TIME).gte(startTime).lte(endTime)));
            } else {
                list.add(Aggregation.match(Criteria.where(CREATE_TIME).gte(startTime)));
            }
        }
        list.add(Aggregation.match(Criteria.where(USER_TYPE).is(UserType.USER.name())));
        list.add(Aggregation.project()
                .and(CREATE_TIME).as("time")
                .andExclude(ID)
        );
        return aggregat(list, T_USER);
    }

    @Override
    public void disable(String id, Boolean flag) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.ID).is(id));
        Update update = new Update();
        update.set(ModelConstant.USER_USER_FLAG, !flag);
        template.updateFirst(query, update, User.class);
    }
}
