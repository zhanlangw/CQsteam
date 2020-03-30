package cn.bytecloud.steam.menu.dao;

import cn.bytecloud.steam.base.dao.BaseDao;
import cn.bytecloud.steam.constant.ModelConstant;
import cn.bytecloud.steam.menu.entity.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class MenuDaoImpl extends BaseDao<Menu> implements MenuDao {
    @Autowired
    private MongoTemplate template;

    @Autowired
    private MenuRepository repository;

    @Override
    public Menu findByPermissionId(String permissionId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.MENU_PERMISSION_IDS).elemMatch(new Criteria().in(permissionId)));
        return template.findOne(query, Menu.class);
    }
}
