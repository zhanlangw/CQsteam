package cn.bytecloud.steam.permission.entity;

import cn.bytecloud.steam.base.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static cn.bytecloud.steam.constant.ModelConstant.*;

/**
 * 权限
 */
@Data
@Document(collection = T_PERMISSON)
public class Permission extends BaseEntity {
    //名字
    @Field(PERMISSON_NAME)
    private String name;

    //接口地址
    @Field(PERMISSON_INTERFACE_URL)
    private String interfaceUrl;
}
