package cn.bytecloud.steam.advert.service;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.advert.dto.AddAdvertDto;
import cn.bytecloud.steam.advert.dto.AdvertDto;

import java.util.Map;

public interface AdvertService {
    Object list();

    void upd(AdvertDto dto);

    Object item(String id);

    Map add(AddAdvertDto dto);

    Object showAdvert(Integer type);

    void del(String id) throws ByteException;
}
