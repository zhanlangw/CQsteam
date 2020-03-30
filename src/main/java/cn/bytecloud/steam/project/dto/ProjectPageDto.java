package cn.bytecloud.steam.project.dto;

import cn.bytecloud.steam.base.dto.BasePageDto;
import lombok.Data;

@Data
public class ProjectPageDto extends BasePageDto {
    private String number;
    private String name;
    private String prize;
    private Boolean submitFlag;

    private String category;
    private Integer group;
    private String district;
    private String school;
    private Boolean docFlag;
}
