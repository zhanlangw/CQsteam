package cn.bytecloud.steam.category.dao;

import cn.bytecloud.steam.category.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    List<Category> findByName(String name);

    List<Category> findByNameOrAbbreviation(String name, String abbreviation);

    List<Category> findByAbbreviation(String abbreviation);
}
