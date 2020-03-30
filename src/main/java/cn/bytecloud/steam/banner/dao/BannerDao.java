package cn.bytecloud.steam.banner.dao;

import cn.bytecloud.steam.banner.entity.Banner;

import java.util.List;

public interface BannerDao {
    Banner save(Banner banner);

    Banner findOne(String id);

    void del(String id);

    List<Banner> homeList();

}
