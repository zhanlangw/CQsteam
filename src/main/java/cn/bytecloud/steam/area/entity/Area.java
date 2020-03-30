package cn.bytecloud.steam.area.entity;

import cn.bytecloud.steam.base.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static cn.bytecloud.steam.constant.ModelConstant.*;

/**
 * 区域
 */
@Document(collection = T_AREAT)
@Data
public class Area {
    @Id
    private String id;

    //名称
    @Field(AREA_NAME)
    private String name;

    //上级id
    @Field(AREA_PID)
    private String parentId;
}
