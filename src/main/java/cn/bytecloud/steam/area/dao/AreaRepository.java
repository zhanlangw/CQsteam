package cn.bytecloud.steam.area.dao;

import cn.bytecloud.steam.area.entity.Area;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface AreaRepository extends MongoRepository<Area, String> {
    Area findByName(String name);

    Area findByNameAndParentId(String name, String parentId);

    List<Area> findByParentId(String areaId);
}
