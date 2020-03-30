package cn.bytecloud.steam.role.entity;

import cn.bytecloud.steam.base.entity.BaseEntity;
import cn.bytecloud.steam.menu.entity.Menu;
import cn.bytecloud.steam.menu.service.MenuService;
import cn.bytecloud.steam.permission.entity.Permission;
import cn.bytecloud.steam.permission.service.PermissionService;
import cn.bytecloud.steam.util.SpringUtils;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.*;

import static cn.bytecloud.steam.constant.ModelConstant.*;

/**
 * 角色
 */
@Data
@Document(collection = T_ROLE)
public class Role extends BaseEntity {
    //名称
    @Field(ROLE_NAME)
    private String name;
    //权限集合
    @Field(ROLE_PERMISSIONS)
    private Set<String> permissions = new HashSet<>();

    public Map toDto() {
        Map map = new HashMap();
        map.put("id", super.getId());
        map.put("name", name);
        PermissionService permissionService = SpringUtils.getBean(PermissionService.class);
        MenuService menuService = SpringUtils.getBean(MenuService.class);
        List permissions = new ArrayList();
        this.permissions.forEach(url -> {
            Permission permission = permissionService.findFirstByInterfaceUrl(url);
            Menu menu = menuService.findByPermissionId(permission.getId());
            Map item = new HashMap();
            item.put("interfaceUrl", permission.getInterfaceUrl());
            item.put("name", permission.getName());
            item.put("menuId", menu.getId());
            item.put("menuName", menu.getName());
            permissions.add(item);
        });
        map.put("permissions", permissions);
        return map;
    }
}
