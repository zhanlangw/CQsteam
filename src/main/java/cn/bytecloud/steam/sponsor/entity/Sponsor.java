package cn.bytecloud.steam.sponsor.entity;

import cn.bytecloud.steam.base.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashMap;
import java.util.Map;

import static cn.bytecloud.steam.constant.ModelConstant.*;

/**
 * 赞助商
 */
@Data
@Document(collection = T_SPONSOR)
public class Sponsor extends BaseEntity {
    //图片地址
    @Field(SPONSOR_PATH)
    private String path;

    //跳转链接
    @Field(SPONSOR_url)
    private String url;

    public Map toDto() {
        Map map = new HashMap();
        map.put("id", super.getId());
        map.put("url", url);
        map.put("path", path);
        return map;
    }
}
