package cn.bytecloud.steam.user.dto;

import cn.bytecloud.steam.user.entity.User;
import cn.bytecloud.steam.user.entity.UserType;
import cn.bytecloud.steam.util.MD5Util;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.HashSet;


@Data
public class UserDto {
    @NotEmpty
    private String username;

    @NotEmpty
    @Length(max = 16, min = 8)
    private String password;

    @NotEmpty
    private String captcha;

    public User toData() {
        User user = new User();
        user.setUsername(username);
        user.setPassword(MD5Util.getMD5(password));
        user.setType(UserType.USER);
        return user;
    }
}
