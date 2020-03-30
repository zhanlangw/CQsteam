package cn.bytecloud.steam.competitionMsg.service;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.exception.ErrorCode;
import cn.bytecloud.steam.competitionMsg.dao.CompetitionMsgDao;
import cn.bytecloud.steam.competitionMsg.dao.CompetitionMsgRepository;
import cn.bytecloud.steam.competitionMsg.dto.CompetitionDto;
import cn.bytecloud.steam.competitionMsg.entity.CompetitionMsg;
import cn.bytecloud.steam.competitionMsg.entity.CompetitionType;
import cn.bytecloud.steam.util.EmptyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CompetitionServiceImpl implements CompetitionService {
    @Autowired
    private CompetitionMsgDao dao;

    @Autowired
    private CompetitionMsgRepository repository;

    /**
     * 大赛详情
     *
     * @param type
     * @return
     */
    @Override
    public Object itme(CompetitionType type) {
        CompetitionMsg competitionMsg = dao.findOneByType(type);
        return competitionMsg.toDto();
    }

    @Override
    public Object upd(CompetitionDto dto, CompetitionType type) throws ByteException {
        if (type == CompetitionType.NOTICE && EmptyUtil.isEmpty(dto.getPath())) {
            throw new ByteException(ErrorCode.NULL_PARAMETER, new String[]{"path"});
        }
        dao.update(dto, type);
        return itme(type);
    }

    @Override
    public Object homeNotice(CompetitionType type) {
        CompetitionMsg competitionMsg = repository.findOneByType(type);
        Map map = new HashMap();
        map.put("title", competitionMsg.getTitle());
        map.put("content", competitionMsg.getContent());
        if (type == CompetitionType.NOTICE) {
            map.put("path", competitionMsg.getPath());
        }
        return map;

    }
}
