package cn.bytecloud.steam.sponsor.dao;

import cn.bytecloud.steam.sponsor.dto.SponsorPageDto;
import cn.bytecloud.steam.sponsor.entity.Sponsor;

public interface SponsorDao {
    Sponsor save(Sponsor dto);

    Object list(SponsorPageDto dto);
}
