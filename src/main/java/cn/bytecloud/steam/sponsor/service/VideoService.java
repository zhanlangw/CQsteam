package cn.bytecloud.steam.sponsor.service;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.sponsor.dto.AddSponsorDto;
import cn.bytecloud.steam.sponsor.dto.SponsorPageDto;

public interface VideoService {
    Object sava(AddSponsorDto dto);

    void del(String id) throws ByteException;

    Object list(SponsorPageDto dto);

    Object item(String id);
}
