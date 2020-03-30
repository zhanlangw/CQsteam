package cn.bytecloud.steam.competitionMsg.dao;

import cn.bytecloud.steam.competitionMsg.dto.CompetitionDto;
import cn.bytecloud.steam.competitionMsg.entity.CompetitionMsg;
import cn.bytecloud.steam.competitionMsg.entity.CompetitionType;

import java.util.List;

public interface CompetitionMsgDao {
    void save(CompetitionMsg msg);

    List<CompetitionMsg> findAll();

    CompetitionMsg findOneByType(CompetitionType type);

    void update(CompetitionDto dto, final CompetitionType type);

}
