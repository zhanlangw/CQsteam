package cn.bytecloud.steam.init;

import cn.bytecloud.steam.advert.dao.AdvertRepository;
import cn.bytecloud.steam.advert.entity.Advert;
import cn.bytecloud.steam.advert.entity.AdvertType;
import cn.bytecloud.steam.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class InitAdvert {
    @Autowired
    private AdvertRepository repository;

    @PostConstruct
    public void init() {
        List<AdvertType> list = new ArrayList<>();
        list.add(AdvertType.BASIS);
        list.add(AdvertType.SUBMIT);
        repository.findAll().forEach(item -> list.remove(item.getType()));

        list.forEach(item -> {
            Advert advert = new Advert();
            advert.setType(item);
            advert.setId(UUIDUtil.getUUID());
            advert.setUrl("没有");
            advert.setImagePath("没有");
            repository.save(advert);
        });
    }
}
