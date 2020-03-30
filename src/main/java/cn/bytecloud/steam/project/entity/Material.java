package cn.bytecloud.steam.project.entity;

import cn.bytecloud.steam.category.entity.StageType;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

import static cn.bytecloud.steam.constant.ModelConstant.*;

@Data
public class Material {

    //阶段
    @Field(PROJECT_MATERIAL_STAGE_TYPE)
    private StageType stageType;

    //ppt地址
    @Field(PROJECT_MATERIAL_PPT_PATH)
    private List<String> pptPath = new ArrayList<>();

    //doc地址
    @Field(PROJECT_MATERIAL_DOC_PATHS)
    private List<String> docPath = new ArrayList<>();

    //视屏地址
    @Field(PROJECT_MATERIAL_VIDEO_PATH)
    private List<String> videoPath = new ArrayList<>();

    //图片地址
    @Field(PROJECT_MATERIAL_IMAGE_PATHS)
    private List<String> imagePath = new ArrayList<>();

}
