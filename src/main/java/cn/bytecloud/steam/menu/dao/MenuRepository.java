package cn.bytecloud.steam.menu.dao;

import cn.bytecloud.steam.menu.entity.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends MongoRepository<Menu, String> {
    Menu findFirstByName(String name);
}
