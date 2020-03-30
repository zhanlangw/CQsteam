package cn.bytecloud.steam.init;

import cn.bytecloud.steam.export.thread.ZipHandler;
import cn.bytecloud.steam.user.dao.UserRepository;
import cn.bytecloud.steam.user.entity.User;
import cn.bytecloud.steam.user.entity.UserType;
import cn.bytecloud.steam.user.thread.SMSHandler;
import cn.bytecloud.steam.util.MD5Util;
import cn.bytecloud.steam.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class InitUser {
    @Autowired
    private UserRepository repository;

    @Autowired
    private SMSHandler handler;

    @Autowired
    private ZipHandler zipHandler;

    @PostConstruct
    public void initUser() {
        new Thread(handler.getInstance()).start();
        new Thread(zipHandler.getInstance()).start();
        log.info("短信处理启动");

        if (null == repository.findFirstByType(UserType.ROOT)) {
            User user = new User();
            user.setName("超级管理员");
            user.setUsername("root");
            user.setUserflag(true);
            user.setId(UUIDUtil.getUUID());
            user.setPassword(MD5Util.getMD5("cqzkp@0402"));
            user.setType(UserType.ROOT);
            user.setCreateTime(System.currentTimeMillis());
            repository.save(user);
        }
    }
}
