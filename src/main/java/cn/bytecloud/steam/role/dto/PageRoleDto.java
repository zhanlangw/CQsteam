package cn.bytecloud.steam.role.dto;

import cn.bytecloud.steam.base.dto.BasePageDto;
import lombok.Data;

@Data
public class PageRoleDto extends BasePageDto {

    private String name;

    private String creator;
}
