package cn.bytecloud.steam.sponsor.dto;

import cn.bytecloud.steam.sponsor.entity.Sponsor;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class UpdSponsorDto extends AddSponsorDto {

    @NotEmpty
    private String id;

    @Override
    public Sponsor toData() {
        Sponsor video = super.toData();
        video.setId(id);
        return video;
    }
}
