package cn.bytecloud.steam.competitionMsg.dao;

import cn.bytecloud.steam.competitionMsg.entity.CompetitionMsg;
import cn.bytecloud.steam.competitionMsg.entity.CompetitionType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetitionMsgRepository extends MongoRepository<CompetitionMsg, String> {

    CompetitionMsg findOneByType(CompetitionType type);
}

