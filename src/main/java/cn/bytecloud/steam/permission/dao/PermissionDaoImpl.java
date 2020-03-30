package cn.bytecloud.steam.permission.dao;

import cn.bytecloud.steam.base.dao.BaseDao;
import cn.bytecloud.steam.constant.ModelConstant;
import cn.bytecloud.steam.permission.entity.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PermissionDaoImpl extends BaseDao implements PermissionDao {
    @Autowired
    private MongoTemplate template;
    @Autowired
    private PermissionRepository repository;

    @Override
    public List<Permission> findByIds(List<String> permissionIds) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.ID).in(permissionIds));
        return template.find(query, Permission.class);
    }
}
