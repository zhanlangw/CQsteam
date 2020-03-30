package cn.bytecloud.steam.project.dao;

import cn.bytecloud.steam.project.entity.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {
    List<Project> findByCategoryId(String categoryId);

    Project findFirstByNumber(String number);

    List<Project> findByDocsubmitFlag(boolean flag);

    List<Project> findByZipFlag(boolean flag);
}
