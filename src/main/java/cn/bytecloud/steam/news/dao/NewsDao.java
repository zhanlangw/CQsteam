package cn.bytecloud.steam.news.dao;

import cn.bytecloud.steam.base.dto.BasePageDto;
import cn.bytecloud.steam.base.dto.PageModel;
import cn.bytecloud.steam.news.dto.NewsPageDto;
import cn.bytecloud.steam.news.entity.News;

public interface NewsDao {
    News save(News news);

    Object list(NewsPageDto dto);

    PageModel<News> homeList(BasePageDto dto);
}
