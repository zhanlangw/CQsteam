package cn.bytecloud.steam.news.entity;

import cn.bytecloud.steam.base.entity.BaseEntity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashMap;
import java.util.Map;

import static cn.bytecloud.steam.constant.ModelConstant.*;

/**
 * 新闻
 */
@Data
@Document(collection = T_NEWS)
public class News extends BaseEntity {
    //标题
    @Field(NEWS_TITLE)
    private String title;

    //内容
    @Field(NEWS_CONTENT)
    private String content;

    //文件地址
    @Field(NEWS_APTH)
    private String path;

    public Map toDto() {
        Map map = new HashMap();
        map.put("id", super.getId());
        map.put("title", title);
        map.put("content", content);
        map.put("path", path);
        return map;
    }
}
