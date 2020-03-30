package cn.bytecloud.steam.init;

import cn.bytecloud.steam.competitionMsg.dao.CompetitionMsgDao;
import cn.bytecloud.steam.competitionMsg.entity.CompetitionMsg;
import cn.bytecloud.steam.competitionMsg.entity.CompetitionType;
import cn.bytecloud.steam.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class Competition {

    @Autowired
    private CompetitionMsgDao dao;

    @PostConstruct
    public void init() {
        List<CompetitionType> list = new ArrayList<>();
        list.add(CompetitionType.INTRODUCTION);
        list.add(CompetitionType.NOTICE);
        dao.findAll().forEach(item -> {
            list.remove(item.getType());
        });
        list.forEach(item -> {
            init(dao, item);
        });
    }

    public void init(CompetitionMsgDao dao, CompetitionType type) {
        CompetitionMsg msg = new CompetitionMsg();
        msg.setType(type);
        msg.setId(UUIDUtil.getUUID());
        msg.setTitle("主题");
        msg.setContent("内容");
        msg.setUpdateTime(System.currentTimeMillis());
        msg.setCreateTime(System.currentTimeMillis());
        dao.save(msg);
    }
}
