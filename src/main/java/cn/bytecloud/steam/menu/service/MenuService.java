package cn.bytecloud.steam.menu.service;

import cn.bytecloud.steam.menu.entity.Menu;

public interface MenuService {
    Menu findFirstByName(String name);

    void save(Menu tMenu);

    Menu findByPermissionId(String id);

    Object list();

}
