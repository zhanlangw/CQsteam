package cn.bytecloud.steam.permission.dao;

import cn.bytecloud.steam.permission.entity.Permission;

import java.util.List;

public interface PermissionDao {
    List<Permission> findByIds(List<String> permissionIds);
}
