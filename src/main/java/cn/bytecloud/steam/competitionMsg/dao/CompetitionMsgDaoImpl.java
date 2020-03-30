package cn.bytecloud.steam.competitionMsg.dao;

import cn.bytecloud.steam.competitionMsg.dto.CompetitionDto;
import cn.bytecloud.steam.competitionMsg.entity.CompetitionMsg;
import cn.bytecloud.steam.competitionMsg.entity.CompetitionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

import static cn.bytecloud.steam.constant.ModelConstant.*;

@Repository
public class CompetitionMsgDaoImpl implements CompetitionMsgDao {
    @Autowired
    private CompetitionMsgRepository repository;
    @Autowired
    private MongoTemplate template;

    @Override
    public void save(final CompetitionMsg msg) {
        repository.save(msg);
    }

    @Override
    public List<CompetitionMsg> findAll() {
        return repository.findAll();
    }

    @Override
    public CompetitionMsg findOneByType(CompetitionType type) {
        return repository.findOneByType(type);
    }

    @Override
    public void update(final CompetitionDto dto, final CompetitionType type) {
        Query query = new Query();
        Update update = new Update();
        query.addCriteria(Criteria.where(COMPETITION_TYPE).is(type));
        update.set(COMPETITION_PATH, dto.getPath());
        update.set(COMPETITION_TITLE, dto.getTitle());
        update.set(COMPETITION_CONTENT, dto.getContent());
        template.updateFirst(query, update, CompetitionMsg.class);
    }
}
