package cn.bytecloud.steam.video.dto;

import cn.bytecloud.steam.video.entity.Video;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class AddVideoDto {
    @NotEmpty
    @Length(max = 50)
    private String title;

    @NotEmpty
    private String path;

    public Video toData() {
        Video video = new Video();
        video.setPath(path);
        video.setTitle(title);
        return video;
    }
}
