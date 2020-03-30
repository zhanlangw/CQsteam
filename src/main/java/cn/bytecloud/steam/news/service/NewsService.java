package cn.bytecloud.steam.news.service;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.base.dto.BasePageDto;
import cn.bytecloud.steam.news.dto.AddNewsDto;
import cn.bytecloud.steam.news.dto.NewsPageDto;

public interface NewsService {
    Object save(AddNewsDto dto);

    Object item(String id);

    void del(String id) throws ByteException;

    Object list(NewsPageDto dto);

    Object homeList(BasePageDto dto);

}
