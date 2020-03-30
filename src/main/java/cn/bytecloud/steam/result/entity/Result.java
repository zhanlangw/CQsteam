package cn.bytecloud.steam.result.entity;

import cn.bytecloud.steam.base.entity.BaseEntity;
import cn.bytecloud.steam.constant.ModelConstant;
import cn.bytecloud.steam.util.PathUtil;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashMap;
import java.util.Map;

/**
 * 比赛结果
 */
@Data
@Document(collection = ModelConstant.T_RESULT)
public class Result extends BaseEntity {
    //标题
    @Field(ModelConstant.RULE_TITLE)
    private String title;

    //文件地址
    @Field(ModelConstant.RESULT_PATH)
    private String path;

    public Map toDto() {
        Map map = new HashMap();
        map.put("id", super.getId());
        map.put("title", title);
        map.put("path", path);
        map.put("fileName", PathUtil.getFileName(path));
        return map;
    }

}
