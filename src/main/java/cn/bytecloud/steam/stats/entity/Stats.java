package cn.bytecloud.steam.stats.entity;

import cn.bytecloud.steam.base.entity.BaseEntity;
import cn.bytecloud.steam.constant.ModelConstant;
import cn.bytecloud.steam.util.StringUtil;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static cn.bytecloud.steam.constant.ModelConstant.*;

/**
 * 统计
 */
@Data
@Document(collection = T_STATS)
public class Stats {

    @Id
    private String id;

    //用户名
    @Field(STATS_USERNAME)
    private String username;

    //浏览器id
    @Field(STATS_BID)
    private String bid;

    //时间
    @Field(STATS_TIME)
    private long time;
}
