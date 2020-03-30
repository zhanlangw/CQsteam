package cn.bytecloud.steam.role.dao;

import cn.bytecloud.steam.role.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {

    Role findFirstByName(String name);

    List<Role> findByName(String name);
}

