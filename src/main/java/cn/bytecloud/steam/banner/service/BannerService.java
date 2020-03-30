package cn.bytecloud.steam.banner.service;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.banner.dto.AddBannerDto;
import cn.bytecloud.steam.banner.dto.BannerPageDto;

public interface BannerService {
    Object save(AddBannerDto dto) throws ByteException;

    void del(String id) throws ByteException;

    Object item(String id);

    Object list(BannerPageDto dto);

    Object homeList();

}
