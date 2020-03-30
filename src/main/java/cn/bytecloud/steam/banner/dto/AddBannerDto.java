package cn.bytecloud.steam.banner.dto;

import cn.bytecloud.steam.banner.entity.Banner;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
public class AddBannerDto {
    @NotEmpty
    private String title;

    @NotNull
    private Integer type;

    @NotEmpty
    private String bigImagePath;

    @NotEmpty
    private String smallImagePath;

//    @NotEmpty
    private String url;

    @NotNull
    private Integer number;

    //    public String getUrl() {
//        this.url = url.startsWith("/") ? this.url : "/" + this.url;
//        return IpUtil.getSerIp() + ":" + SpringUtils.getBean(SystemConstant.class).port + this.url;
//    }
    public Banner toData() {
        Banner banner = new Banner();
        banner.setTitle(title);
        banner.setBigImagePath(bigImagePath);
        banner.setSmallImagePath(smallImagePath);
        banner.setUrl(url);
        banner.setType(type);
        banner.setNumber(number);
        return banner;
    }
}
