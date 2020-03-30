package cn.bytecloud.steam.news.service;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.base.dto.BasePageDto;
import cn.bytecloud.steam.base.dto.PageModel;
import cn.bytecloud.steam.file.service.FileService;
import cn.bytecloud.steam.news.dao.NewsDao;
import cn.bytecloud.steam.news.dao.NewsRepository;
import cn.bytecloud.steam.news.dto.AddNewsDto;
import cn.bytecloud.steam.news.dto.NewsPageDto;
import cn.bytecloud.steam.news.entity.News;
import cn.bytecloud.steam.util.EmptyUtil;
import cn.bytecloud.steam.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NewsServiceImpl implements NewsService {
    @Autowired
    private NewsDao dao;

    @Autowired
    private NewsRepository repository;

    @Autowired
    private FileService fileService;
    @Override
    public Object save(AddNewsDto dto) {
        return dao.save(dto.toData()).toDto();
    }

    @Override
    public Object item(String id) {
        return repository.findOne(id).toDto();
    }

    @Override
    public void del(String id) throws ByteException {
        News news = repository.findOne(id);
        if (EmptyUtil.isNotEmpty(news.getPath())) {
            fileService.deleteFile(news.getPath());
        }
        repository.delete(id);
    }

    @Override
    public Object list(NewsPageDto dto) {
        return dao.list(dto);
    }

    @Override
    public Object homeList(BasePageDto dto) {
        List list = new ArrayList();
        PageModel<News> newsPageModel = dao.homeList(dto);
        for (News news : newsPageModel.getValue()) {
            Map map = new HashMap();
            map.put("id", news.getId());
            map.put("title", news.getTitle());
            map.put("createTime", StringUtil.getTime(new Date(news.getCreateTime())));
            list.add(map);
        }
        return new PageModel<>(newsPageModel.getTotalCount(), list);

    }
}
