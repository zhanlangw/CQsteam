package cn.bytecloud.steam.event.entity;

import cn.bytecloud.steam.base.entity.BaseEntity;
import cn.bytecloud.steam.util.StringUtil;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static cn.bytecloud.steam.constant.ModelConstant.*;

/**
 * 活动日历
 */
@Data
@Document(collection = T_EVENT)
public class Event extends BaseEntity {
    //开始时间
    @Field(EVENT_START_TIME)
    private long startTime;

    //结束时间
    @Field(EVENT_END_TIME)
    private long endTime;

    //地址
    @Field(EVENT_ADDRESS)
    private String address;

    //阶段
    @Field(EVENT_STAGE)
    private String stage;

    //标题
    @Field(EVENT_TITLE)
    private String title;

    public Map toDto() {
        Map map = new HashMap();
        map.put("id", super.getId());
        map.put("title", title);
        map.put("address", address);
        map.put("stage", stage);
        map.put("startTime", StringUtil.getTime(new Date(startTime)));
        map.put("endTime", StringUtil.getTime(new Date(endTime)));
        return map;
    }
}
