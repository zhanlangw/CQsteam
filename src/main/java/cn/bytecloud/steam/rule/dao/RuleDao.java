package cn.bytecloud.steam.rule.dao;

import cn.bytecloud.steam.base.dto.PageModel;
import cn.bytecloud.steam.rule.dto.RulePageDto;
import cn.bytecloud.steam.rule.entity.Rule;

import java.util.List;

public interface RuleDao {
    Rule save(Rule rule);

    Rule findOne(String id);

    void del(String id);

    PageModel list(RulePageDto dto);

    List<Rule> homeList();

}
