package cn.bytecloud.steam.video.dao;

import cn.bytecloud.steam.video.dto.VideoPageDto;
import cn.bytecloud.steam.video.entity.Video;

public interface VideoDao {
    Video save(Video dto);

    Object list(VideoPageDto dto);
}
