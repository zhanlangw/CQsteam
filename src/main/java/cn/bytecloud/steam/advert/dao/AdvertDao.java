package cn.bytecloud.steam.advert.dao;

import cn.bytecloud.steam.advert.entity.Advert;

import java.util.List;

public interface AdvertDao {
    List<Advert> findAll();

    Advert findOne(String id);

    void upd(Advert advert);
}
