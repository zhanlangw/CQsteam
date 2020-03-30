package cn.bytecloud.steam.video.entity;

import cn.bytecloud.steam.base.entity.BaseEntity;
import cn.bytecloud.steam.constant.ModelConstant;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashMap;
import java.util.Map;

import static cn.bytecloud.steam.constant.ModelConstant.*;

/**
 * 视屏
 */
@Data
@Document(collection = T_VIDEO)
public class Video extends BaseEntity {
    //视屏地址
    @Field(VIDEO_PATH)
    private String path;

    //标题
    @Field(VIDEO_TITLE)
    private String title;

    public Map toDto() {
        Map map = new HashMap();
        map.put("id", super.getId());
        map.put("title", title);
        map.put("path", path);
        return map;
    }
}
