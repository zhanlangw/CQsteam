package cn.bytecloud.steam.permission.dao;

import cn.bytecloud.steam.permission.entity.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends MongoRepository<Permission, String> {
    Permission findFirstByInterfaceUrl(String interfaceUrl);
}
