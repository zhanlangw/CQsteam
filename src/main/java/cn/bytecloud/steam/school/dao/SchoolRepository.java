package cn.bytecloud.steam.school.dao;

import cn.bytecloud.steam.category.entity.GroupType;
import cn.bytecloud.steam.school.entity.School;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolRepository extends MongoRepository<School, String> {
    List<School> findByAreaIdAndName(String areaId, String name);

    List<School> findByName(String name);
    List<School> findByAreaIdAndNameAndGroup(String areaId, String name, GroupType type);
}
