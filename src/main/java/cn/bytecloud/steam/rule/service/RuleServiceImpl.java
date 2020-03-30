package cn.bytecloud.steam.rule.service;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.file.service.FileService;
import cn.bytecloud.steam.rule.dao.RuleDao;
import cn.bytecloud.steam.rule.dao.RuleRepository;
import cn.bytecloud.steam.rule.dto.RuleDto;
import cn.bytecloud.steam.rule.dto.RulePageDto;
import cn.bytecloud.steam.rule.entity.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RuleServiceImpl implements RuleService {
    @Autowired
    private RuleDao dao;

    @Autowired
    private RuleRepository repository;

    @Autowired
    private FileService fileService;

    /**
     * 保存/修改
     *
     * @param dto
     */
    @Override
    public Map save(RuleDto dto) {
        Rule rule = dao.save(dto.toData());
        return item(rule.getId());
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public Map item(String id) {
        Rule rule = dao.findOne(id);
        return rule.toDto();
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void del(String id) throws ByteException {
        Rule rule = repository.findOne(id);
        fileService.deleteFile(rule.getPath());
        fileService.deleteFile(rule.getImagePath());
        dao.del(id);
    }

    @Override
    public Object list(RulePageDto dto) {
        return dao.list(dto);
    }

    @Override
    public Object homeList() {
        List list = new ArrayList();
        dao.homeList().forEach(item -> {
            Map map = new HashMap();
            map.put("id", item.getId());
            map.put("title", item.getTitle());
            map.put("imagePath", item.getImagePath());
            list.add(map);
        });
        return list;
    }

    @Override
    public Object homeItem(String id) {
        Rule item = repository.findOne(id);
        Map map = new HashMap();
        map.put("id", item.getId());
        map.put("title", item.getTitle());
        map.put("imagePath", item.getImagePath());
        map.put("path", item.getPath());
        map.put("content", item.getContent());
        return map;
    }


}
