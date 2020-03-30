package cn.bytecloud.steam.result.dto;

import cn.bytecloud.steam.base.dto.BasePageDto;
import lombok.Data;

@Data
public class ResultPageDto extends BasePageDto {
    private String title;
    private String creator;
}
