package cn.bytecloud.steam.competitionMsg.service;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.competitionMsg.dto.CompetitionDto;
import cn.bytecloud.steam.competitionMsg.entity.CompetitionType;

public interface CompetitionService {
    Object itme(CompetitionType introduction);

    Object upd(CompetitionDto introduction, final CompetitionType notice) throws ByteException;

    Object homeNotice(CompetitionType notice);
}
