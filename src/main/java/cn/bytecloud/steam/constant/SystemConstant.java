package cn.bytecloud.steam.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SystemConstant {

    public static final String ACTIVE = "spring.profiles.active";
    public static final String DATABASE = "spring.data.mongodb.database";
    public static final String USERNAME = "spring.data.mongodb.username";
    public static final String PASSWORD = "spring.data.mongodb.password";
    public static final String AUTHENTICATION_DATABASE = "spring.data.mongodb.authentication-database";
    public static final String MONGODB_HOST = "spring.data.mongodb.host";
    public static final String MONGODB_PORT = "spring.data.mongodb.port";

    @Value("${server.port}")
    public Integer port;

    @Value("${spring.profiles.active}")
    public String active;

}
