package cn.bytecloud.steam.sponsor.dto;

import cn.bytecloud.steam.sponsor.entity.Sponsor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class AddSponsorDto {
    @NotEmpty
    @Length(max = 50)
    private String url;

    @NotEmpty
    private String path;

    public Sponsor toData() {
        Sponsor video = new Sponsor();
        video.setPath(path);
        video.setUrl(url);
        return video;
    }
}
