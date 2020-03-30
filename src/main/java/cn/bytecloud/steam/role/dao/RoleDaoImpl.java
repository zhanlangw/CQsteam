package cn.bytecloud.steam.role.dao;

import cn.bytecloud.steam.base.dao.BaseDao;
import cn.bytecloud.steam.role.dto.PageRoleDto;
import cn.bytecloud.steam.role.entity.Role;
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
import java.util.Set;

import static cn.bytecloud.steam.constant.ModelConstant.*;

@Repository
public class RoleDaoImpl extends BaseDao implements RoleDao {
    @Autowired
    private MongoTemplate template;
    @Autowired
    private RoleRepository repository;

    @Override
    public List<Role> findByIds(Set<String> roleIds) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(roleIds));
        List<Role> list = template.find(query, Role.class);
        return list;
    }

    @Override
    public Role save(Role role) {
        if (EmptyUtil.isEmpty(role.getId())) {
            role.setId(UUIDUtil.getUUID());
            role.setCreateTime(System.currentTimeMillis());
            role.setCreatorId(UserUtil.getUserId());
        } else {
            Role old = repository.findOne(role.getId());
            role.setCreatorId(old.getCreatorId());
            role.setCreateTime(old.getCreateTime());
        }
        role.setUpdateTime(System.currentTimeMillis());
        repository.save(role);
        return role;
    }

    @Override
    public Object list(PageRoleDto dto) {
        List<AggregationOperation> list = addMatch(dto, CREATE_TIME);

        if (EmptyUtil.isNotEmpty(dto.getName())) {
            list.add(Aggregation.match(Criteria.where(ROLE_NAME).regex(dto.getName())));
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
                .and(ROLE_NAME).as("name")
                .and("user." + USER_NAME).as("creator")
                .and(CREATE_TIME).as("createTime")
                .and(ID).as("id")
                .andExclude(ID)
        );

        return pageList(list, dto, T_ROLE, CREATE_TIME);
    }
}
