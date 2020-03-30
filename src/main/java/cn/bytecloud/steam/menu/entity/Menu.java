package cn.bytecloud.steam.menu.entity;

import cn.bytecloud.steam.base.entity.BaseEntity;
import cn.bytecloud.steam.constant.ModelConstant;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

import static cn.bytecloud.steam.constant.ModelConstant.*;

/**
 * 菜单
 */
@Document(collection = T_MENU)
@Data
public class Menu extends BaseEntity {
    //名称
    @Field(MENU_NAME)
    private String name;

    //权限集合
    @Field(MENU_PERMISSION_IDS)
    private List<String> permissionIds = new ArrayList<>();
}
