package cn.bytecloud.steam.project.dto;

import cn.bytecloud.steam.category.entity.StageType;
import cn.bytecloud.steam.project.entity.Material;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static cn.bytecloud.steam.constant.ModelConstant.*;
import static cn.bytecloud.steam.constant.ModelConstant.PROJECT_MATERIAL_IMAGE_PATHS;

@Data
public class AddMaterialDto {

    @NotEmpty
    private String id;

    @NotNull
    private Integer stageType;

//    @NotEmpty
    private List<String> pptPath = new ArrayList<>();

//    @NotEmpty
    private List<String> docPath = new ArrayList<>();

//    @NotEmpty
    private List<String> videoPath = new ArrayList<>();

//    @NotEmpty
    private List<String> imagePath = new ArrayList<>();

    public StageType getStageType() {
        return stageType == null ? null : StageType.getType(stageType);
    }

    public Material toData() {
        Material material = new Material();
        material.setPptPath(pptPath);
        material.setDocPath(docPath);
        material.setVideoPath(videoPath);
        material.setImagePath(imagePath);
        material.setStageType(getStageType());
        return material;
    }
}
