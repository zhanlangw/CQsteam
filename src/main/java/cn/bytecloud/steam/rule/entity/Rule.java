package cn.bytecloud.steam.rule.entity;

import cn.bytecloud.steam.base.entity.BaseEntity;
import cn.bytecloud.steam.constant.ModelConstant;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashMap;
import java.util.Map;

import static cn.bytecloud.steam.constant.ModelConstant.*;

/**
 * 规则
 */
@Data
@Document(collection = ModelConstant.T_RULE)
public class Rule extends BaseEntity {
    //编号
    @Field(RULE_NUMBER)
    private Integer number;

    //主题
    @Field(RULE_TITLE)
    private String title;

    //内容
    @Field(RULE_CONTENT)
    private String content;

    //文件地址
    @Field(RULE_PATH)
    private String path;

    //图片地
    @Field(RULE_IMAGE_APTH)
    private String imagePath;

    public Map toDto() {
        Map<String, Object> map = new HashMap();
        map.put("id", super.getId());
        map.put("number", this.number);
        map.put("title", this.title);
        map.put("content", this.content);
        map.put("path", this.path);
        map.put("imagePath", this.imagePath);
        return map;
    }
}
