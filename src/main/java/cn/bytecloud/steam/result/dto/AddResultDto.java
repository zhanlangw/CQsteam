package cn.bytecloud.steam.result.dto;

import cn.bytecloud.steam.result.entity.Result;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class AddResultDto {
    @NotEmpty
    private String title;

    @NotEmpty
    private String path;

    public Result toData() {
        Result result = new Result();
        result.setTitle(title);
        result.setPath(path);
        return result;
    }
}
