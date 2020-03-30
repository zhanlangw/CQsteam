package cn.bytecloud.steam.advert.dao;

import cn.bytecloud.steam.advert.entity.Advert;
import cn.bytecloud.steam.base.dao.BaseDao;
import cn.bytecloud.steam.constant.ModelConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdvertDaoImpl extends BaseDao implements AdvertDao {
    @Autowired
    private MongoTemplate template;

    @Autowired
    private AdvertRepository repository;

    @Override
    public List<Advert> findAll() {
        return repository.findAll(new Sort(Sort.Direction.ASC, ModelConstant.CREATE_TIME));
    }

    @Override
    public Advert findOne(String id) {
        return repository.findOne(id);
    }

    @Override
    public void upd(Advert advert) {
        Query query = new Query();
        Update update = new Update();
        query.addCriteria(Criteria.where("id").is(advert.getId()));
        update.set(ModelConstant.ADVERT_IMAGE_PATH, advert.getImagePath());
        update.set(ModelConstant.ADVERT_URL, advert.getUrl());
        update.set(ModelConstant.UPDATE_TIME, System.currentTimeMillis());
        template.updateFirst(query, update, Advert.class);
    }
}
