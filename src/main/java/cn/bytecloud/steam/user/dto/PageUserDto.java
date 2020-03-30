package cn.bytecloud.steam.user.dto;

import cn.bytecloud.steam.base.dto.BasePageDto;
import lombok.Data;

@Data
public class PageUserDto extends BasePageDto {
    private String name;

    private String creator;
}
