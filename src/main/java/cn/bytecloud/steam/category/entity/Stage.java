package cn.bytecloud.steam.category.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import static cn.bytecloud.steam.constant.ModelConstant.*;

@Data
public class Stage {

    //阶段类型
    @Field(CATEGORY_STAGE_TYPE)
    private StageType type;

    //结束时间
    @Field(CATEGORY_STAGE_END_TIME)
    private long endTime;

    //ppt数量
    @Field(CATEGORY_STAGE_PPT_NUM)
    private Integer pptNum;

    //doc数量
    @Field(CATEGORY_STAGE_DOC_NUM)
    private Integer docNum;

    //视频数量
    @Field(CATEGORY_STAGE_VIDEO_NUM)
    private Integer videoNum;

    //图片数量
    @Field(CATEGORY_STAGE_IMAGE_NUM)
    private Integer imageNum;
}
