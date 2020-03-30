package cn.bytecloud.steam.video.dto;

import cn.bytecloud.steam.video.entity.Video;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class UpdVideoDto extends AddVideoDto {

    @NotEmpty
    private String id;

    @Override
    public Video toData() {
        Video video = super.toData();
        video.setId(id);
        return video;
    }
}
