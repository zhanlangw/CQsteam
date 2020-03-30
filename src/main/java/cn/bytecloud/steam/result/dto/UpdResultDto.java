package cn.bytecloud.steam.result.dto;

import cn.bytecloud.steam.result.entity.Result;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class UpdResultDto extends AddResultDto {

    @NotEmpty
    private String id;

    @Override
    public Result toData() {
        Result result = super.toData();
        result.setId(id);
        return result;
    }
}
