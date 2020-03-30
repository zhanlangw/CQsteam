package cn.bytecloud.steam.banner.dto;

import cn.bytecloud.steam.banner.entity.Banner;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class UpdBannerDto extends AddBannerDto {
    @NotEmpty
    private String id;

    @Override
    public Banner toData() {
        Banner banner = super.toData();
        banner.setId(id);
        return banner;
    }
}
