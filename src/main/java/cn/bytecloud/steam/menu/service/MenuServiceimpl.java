package cn.bytecloud.steam.menu.service;

import cn.bytecloud.steam.annotation.Permission;
import cn.bytecloud.steam.menu.dao.MenuDao;
import cn.bytecloud.steam.menu.dao.MenuRepository;
import cn.bytecloud.steam.menu.entity.Menu;
import cn.bytecloud.steam.permission.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MenuServiceimpl implements MenuService {
    @Autowired
    private MenuDao dao;

    @Autowired
    private MenuRepository repository;

    @Autowired
    private PermissionService permissionService;

    @Override
    public Menu findFirstByName(String name) {
        return repository.findFirstByName(name);
    }

    @Override
    public void save(Menu tMenu) {
        repository.save(tMenu);
    }

    @Override
    public Menu findByPermissionId(String permissionId) {
        return dao.findByPermissionId(permissionId);

    }

    @Override
    public Object list() {
        List<Menu> list = repository.findAll();
        List data = new ArrayList();
        list.forEach(menu -> {
            Map map = new HashMap();
            map.put("title", menu.getName());
            map.put("key", menu.getId());
            List values = new ArrayList();
            permissionService.findByIds(menu.getPermissionIds()).forEach(permission -> {
                Map value = new HashMap();
                value.put("title", permission.getName());
                value.put("key", permission.getInterfaceUrl());
                values.add(value);
            });
            map.put("children", values);
            data.add(map);
        });
        return data;
    }
}
