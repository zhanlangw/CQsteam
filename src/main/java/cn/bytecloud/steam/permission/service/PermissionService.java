package cn.bytecloud.steam.permission.service;

import cn.bytecloud.steam.permission.entity.Permission;

import java.util.List;

public interface PermissionService {
    void save(Permission perm);

    Permission findFirstByInterfaceUrl(String interfaceUrl);

    List<Permission> findByIds(List<String> permissionIds);

}
