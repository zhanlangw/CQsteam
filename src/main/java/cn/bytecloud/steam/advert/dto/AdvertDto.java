package cn.bytecloud.steam.advert.dto;

import cn.bytecloud.steam.advert.entity.Advert;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class AdvertDto extends AddAdvertDto {
    @NotEmpty
    private String id;

    @Override
    public Advert toData() {
        Advert advert = super.toData();
        advert.setId(id);
        return advert;
    }
}
