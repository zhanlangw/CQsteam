package cn.bytecloud.steam.util;

import cn.bytecloud.steam.user.entity.User;
import org.apache.shiro.SecurityUtils;

public class UserUtil {
    public static User getUser() {
        return (User) SecurityUtils.getSubject().getPrincipal();
    }

    public static String getUserId() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return user.getId();
    }
}
