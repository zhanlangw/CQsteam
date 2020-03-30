package cn.bytecloud.steam.competitionMsg.entity;

import cn.bytecloud.steam.base.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashMap;
import java.util.Map;

import static cn.bytecloud.steam.constant.ModelConstant.*;

/**
 * 大赛简介须知
 */
@Data
@Document(collection = T_COMPETITION_MSG)
public class CompetitionMsg extends BaseEntity {
    //标题
    @Field(COMPETITION_TITLE)
    private String title;

    //内容
    @Field(COMPETITION_CONTENT)
    private String content;

    //文件地址
    @Field(COMPETITION_PATH)
    private String path;

    //类型
    @Field(COMPETITION_TYPE)
    private CompetitionType type;

    public Map toDto() {
        Map<String, Object> map = new HashMap();
        map.put("id", super.getId());
        map.put("title", this.title);
        map.put("content", this.content);
        if (CompetitionType.NOTICE == this.type) {
            map.put("path", this.path);
        }
        return map;
    }
}
