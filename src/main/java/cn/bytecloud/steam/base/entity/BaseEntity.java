package cn.bytecloud.steam.base.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

import static cn.bytecloud.steam.constant.ModelConstant.*;

@Data
public class BaseEntity implements Serializable {
    @Id
    private String id;

    @Field(CREATE_TIME)
    private long createTime;

    @Field(UPDATE_TIME)
    private long updateTime;

    @Field(CREATOR_ID)
    private String creatorId;

}
