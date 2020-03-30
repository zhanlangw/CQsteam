package cn.bytecloud.steam.user.entity;

import cn.bytecloud.steam.base.entity.BaseEntity;
import cn.bytecloud.steam.role.entity.Role;
import cn.bytecloud.steam.role.service.RoleService;
import cn.bytecloud.steam.util.SpringUtils;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.*;

import static cn.bytecloud.steam.constant.ModelConstant.*;

/**
 * 用户
 */
@Data
@Document(collection = T_USER)
public class User extends BaseEntity {
    public User() {
        this.userflag = true;
    }

    //名字
    @Field(USER_NAME)
    private String name;

    //类型
    @Field(USER_TYPE)
    private UserType type;

    //登录名
    @Field(USER_USERNAME)
    private String username;
    //密码
    @Field(USER_PASSWORD)
    private String password;

    //是否在用
    @Field(USER_USER_FLAG)
    private boolean userflag;

    //角色集合
    @Field(USER_ROLE_IDS)
    private Set<String> roleIds = new HashSet<>();

    public Map toDto() {
        Map map = new HashMap();
        map.put("id", super.getId());
        map.put("name", name);
        map.put("loginName", username);

        RoleService roleService = SpringUtils.getBean(RoleService.class);
        List role = new ArrayList();

        roleIds.forEach(id -> {
            Map item = new HashMap();
            Role one = roleService.findOne(id);
            item.put("id", one.getId());
            item.put("name", one.getName());
            role.add(item);
        });

        map.put("role", role);
        return map;
    }
}
