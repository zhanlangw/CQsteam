package cn.bytecloud.steam.school.entity;

import cn.bytecloud.steam.base.entity.BaseEntity;
import cn.bytecloud.steam.category.entity.GroupType;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashMap;
import java.util.Map;

import static cn.bytecloud.steam.constant.ModelConstant.*;

/**
 * 学校
 */
@Data
@Document(collection = T_SCHOOL)
public class School extends BaseEntity {
    //名字
    @Field(SCHOOL_NAME)
    private String name;

    //区域
    @Field(SCHOOL_AREA_ID)
    private String areaId;

    //组别
    @Field(SCHOOL_GROUP)
    private GroupType group;

    public Map toDto() {
        Map map = new HashMap();
        map.put("id", super.getId());
        map.put("areaId", areaId);
        map.put("name", name);
        return map;
    }
}
