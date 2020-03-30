package cn.bytecloud.steam.menu.dao;

import cn.bytecloud.steam.menu.entity.Menu;

public interface MenuDao {
    Menu findByPermissionId(String permissionId);
}
