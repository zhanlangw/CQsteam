package cn.bytecloud.steam.advert.dto;

import cn.bytecloud.steam.advert.entity.Advert;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class AddAdvertDto {
//    @NotEmpty
    private String url;

    @NotEmpty
    private String imagePath;

    public Advert toData() {
        Advert advert = new Advert();
        advert.setUrl(url);
        advert.setImagePath(imagePath);
        return advert;
    }
}
