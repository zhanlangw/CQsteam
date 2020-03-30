package cn.bytecloud.steam.video.service;

import cn.bytecloud.steam.video.dto.AddVideoDto;
import cn.bytecloud.steam.video.dto.VideoPageDto;

public interface VideoService {
    Object sava(AddVideoDto dto);

    void del(String id);

    Object list(VideoPageDto dto);

    Object item(String id);
}
