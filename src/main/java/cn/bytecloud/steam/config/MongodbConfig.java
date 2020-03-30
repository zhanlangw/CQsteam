package cn.bytecloud.steam.config;

import cn.bytecloud.steam.util.MD5Util;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.Arrays;

import static cn.bytecloud.steam.constant.SystemConstant.*;

@Configuration
public class MongodbConfig {

    public @Bean
    MongoDbFactory mongoDbFactory(Environment env) {
        MongoClient mongoClient;
        if ("dev".equals(env.getProperty(ACTIVE))) {
            MongoCredential credential = MongoCredential.createCredential(env.getProperty(USERNAME), env.getProperty
                    (AUTHENTICATION_DATABASE), env.getProperty(PASSWORD).toCharArray());
            mongoClient = new MongoClient(new ServerAddress(env.getProperty(MONGODB_HOST), Integer
                    .parseInt(env.getProperty(MONGODB_PORT))),
                    Arrays.asList(credential));
        } else {
            mongoClient = new MongoClient(new ServerAddress(env.getProperty(MONGODB_HOST), Integer
                    .parseInt(env.getProperty(MONGODB_PORT))));
        }
        return new SimpleMongoDbFactory(mongoClient, env.getProperty(DATABASE));
    }

    public @Bean
    MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory) {

        //remove _class
        MappingMongoConverter converter =
                new MappingMongoConverter(mongoDbFactory, new MongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));

        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory, converter);

        return mongoTemplate;
    }
}
