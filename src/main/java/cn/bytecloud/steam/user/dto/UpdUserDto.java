package cn.bytecloud.steam.user.dto;

import cn.bytecloud.steam.user.entity.User;
import cn.bytecloud.steam.util.MD5Util;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.HashSet;
import java.util.Set;

@Data
public class UpdUserDto  {
    @NotEmpty
    private String id;

    @NotEmpty
    @Length(max = 6, min = 2)
    private String name;

    @NotEmpty
    private String loginName;

    @NotEmpty
    private Set<String> roleIds = new HashSet<>();

    public User toData() {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setUsername(loginName);
        user.setRoleIds(roleIds);
        return user;
    }
}
