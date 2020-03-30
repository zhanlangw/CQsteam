package cn.bytecloud.steam.banner.entity;

import cn.bytecloud.steam.base.entity.BaseEntity;
import cn.bytecloud.steam.constant.ModelConstant;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashMap;
import java.util.Map;

import static cn.bytecloud.steam.constant.ModelConstant.*;

/**
 * banner
 */
@Data
@Document(collection = ModelConstant.T_BANNER)
public class Banner extends BaseEntity {

    //标题
    @Field(BANNER_TITLE)
    private String title;

    //类型 1 外部链接，2 跳转规则
    @Field(BANNER_TYPE)
    private Integer type;

    //编号
    @Field(BANNER_NUMBER)
    private Integer number;

    //大图地址
    @Field(BANNER_BIG_IMAGE_PAHT)
    private String bigImagePath;

    //小图地址
    @Field(BANNER_SMALL_IMAGE_PAHT)
    private String smallImagePath;

    //跳转地址
    @Field(BANNER_URL)
    private String url;

    public Map toDto() {
        Map map = new HashMap();
        map.put("id", super.getId());
        map.put("type", type);
        map.put("title", title);
        map.put("number", number);
        map.put("bigImagePath", bigImagePath);
        map.put("smallImagePath", smallImagePath);
        map.put("url", url);
        map.put("number", number);
        return map;
    }

}
