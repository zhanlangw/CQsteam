package cn.bytecloud.steam.rule.service;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.rule.dto.RuleDto;
import cn.bytecloud.steam.rule.dto.RulePageDto;

import java.util.Map;

public interface RuleService {
    Map save(RuleDto dto);

    Map item(String id);

    void del(String id) throws ByteException;

    Object list(RulePageDto dto);

    Object homeList();

    Object homeItem(String id);
}
