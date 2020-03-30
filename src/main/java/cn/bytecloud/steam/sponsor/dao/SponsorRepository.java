package cn.bytecloud.steam.sponsor.dao;

import cn.bytecloud.steam.sponsor.entity.Sponsor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SponsorRepository extends MongoRepository<Sponsor, String> {
}
