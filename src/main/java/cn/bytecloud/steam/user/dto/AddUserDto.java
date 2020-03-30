package cn.bytecloud.steam.user.dto;

import cn.bytecloud.steam.user.entity.User;
import cn.bytecloud.steam.util.MD5Util;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.HashSet;
import java.util.Set;

@Data
public class AddUserDto {
    @NotEmpty
    @Length(max = 6, min = 2)
    private String name;

    @NotEmpty
    private String loginName;

    @NotEmpty
    @Length(max = 16, min = 8)
    private String password;

    @NotEmpty
    private Set<String> roleIds = new HashSet<>();

    public User toData() {
        User user = new User();
        user.setName(name);
        user.setUsername(loginName);
        user.setPassword(MD5Util.getMD5(password));
        user.setRoleIds(roleIds);
        return user;
    }
}
