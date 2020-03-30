package cn.bytecloud.steam.banner.dao;

import cn.bytecloud.steam.banner.entity.Banner;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends MongoRepository<Banner, String> {
}
