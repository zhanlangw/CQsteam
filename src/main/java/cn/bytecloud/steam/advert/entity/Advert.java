package cn.bytecloud.steam.advert.entity;

import cn.bytecloud.steam.base.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashMap;
import java.util.Map;

import static cn.bytecloud.steam.constant.ModelConstant.*;

/**
 * 广告
 */
@Data
@Document(collection = T_ADVERT)
public class Advert extends BaseEntity {

//    @Field(ADVERT_TITLE)
//    private String title;

    //跳轉地址
    @Field(ADVERT_URL)
    private String url;

    //圖片地址
    @Field(ADVERT_IMAGE_PATH)
    private String imagePath;

    //类型
    @Field(ADVERT_TYPE)
    private AdvertType type;

    public Map toDto() {
        Map map = new HashMap();
        map.put("type", type.getEnumType());
        map.put("id", super.getId());
        map.put("url", url);
        map.put("imagePath", imagePath);
        return map;
    }
}
