package cn.bytecloud.steam.category.dto;

import cn.bytecloud.steam.category.entity.Stage;
import cn.bytecloud.steam.category.entity.StageType;
import cn.bytecloud.steam.util.StringUtil;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Objects;

@Data
@Validated
public class StageDto {
    @NotNull
    private Integer type;

    @NotNull
    private String endTime;

    @NotNull
    @Min(0)
    private Integer pptNum;

    @NotNull
    @Min(0)
    private Integer docNum;

    @NotNull
    @Min(0)
    private Integer videoNum;

    @NotNull
    @Min(0)
    private Integer imageNum;


    public Stage toData() {
        Stage stage = new Stage();
        Arrays.stream(StageType.values()).filter(item -> item.getEnumType().equals(type)).findFirst().ifPresent
                (stage::setType);
        stage.setEndTime(Objects.requireNonNull(StringUtil.getTime(endTime)).getTime());
        stage.setPptNum(this.pptNum);
        stage.setDocNum(this.docNum);
        stage.setVideoNum(this.videoNum);
        stage.setImageNum(this.imageNum);
        return stage;
    }
}
