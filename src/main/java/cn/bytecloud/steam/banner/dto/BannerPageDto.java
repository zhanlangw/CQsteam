package cn.bytecloud.steam.banner.dto;

import cn.bytecloud.steam.base.dto.BasePageDto;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class BannerPageDto extends BasePageDto {
    private String title;

    private String creator;
}
