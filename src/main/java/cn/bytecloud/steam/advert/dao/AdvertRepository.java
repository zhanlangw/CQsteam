package cn.bytecloud.steam.advert.dao;

import cn.bytecloud.steam.advert.entity.Advert;
import cn.bytecloud.steam.advert.entity.AdvertType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertRepository extends MongoRepository<Advert, String> {
    List<Advert> findByType(AdvertType type);
}
