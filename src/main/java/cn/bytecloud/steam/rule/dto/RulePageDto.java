package cn.bytecloud.steam.rule.dto;

import cn.bytecloud.steam.base.dto.BasePageDto;
import lombok.Data;

@Data
public class RulePageDto extends BasePageDto {
    private String title;

    private Integer number;

    private String creator;
}
