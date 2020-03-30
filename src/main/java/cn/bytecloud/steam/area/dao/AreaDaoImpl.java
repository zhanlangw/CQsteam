package cn.bytecloud.steam.area.dao;

import cn.bytecloud.steam.area.entity.Area;
import cn.bytecloud.steam.constant.ModelConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AreaDaoImpl implements AreaDao {
    @Autowired
    private AreaRepository repository;
    @Autowired
    private MongoTemplate template;

    @Override
    public Area findOme(final String areaId) {
        return repository.findOne(areaId);
    }

    @Override
    public Object list(String areaIds) {
        List<Map> list = new ArrayList<>();
        List<Area> areaList;
        if ("*".equals(areaIds)) {
            areaList = repository.findByParentId(null);
        } else {
            Query query = new Query();
            query.addCriteria(Criteria.where(ModelConstant.ID).in(Arrays.asList(areaIds.split(","))));
            areaList = template.find(query, Area.class);
        }
        areaList.forEach(item -> {
            Map map = new HashMap();
            map.put("key", item.getId());
            map.put("title", item.getName());
            map.put("uid", item.getParentId());
            map.put("isLeaf", repository.findByParentId(item.getId()).size() > 0 ? false : true);
            list.add(map);
        });
        return list;
    }
}
