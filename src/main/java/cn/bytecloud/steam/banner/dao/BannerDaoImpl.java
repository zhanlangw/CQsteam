package cn.bytecloud.steam.banner.dao;

import cn.bytecloud.steam.base.dao.BaseDao;
import cn.bytecloud.steam.banner.entity.Banner;
import cn.bytecloud.steam.constant.ModelConstant;
import cn.bytecloud.steam.util.EmptyUtil;
import cn.bytecloud.steam.util.UUIDUtil;
import cn.bytecloud.steam.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BannerDaoImpl extends BaseDao<Banner> implements BannerDao {
    @Autowired
    private BannerRepository repository;
    @Autowired
    private MongoTemplate template;

    @Override
    public Banner save(Banner banner) {
        if (EmptyUtil.isEmpty(banner.getId())) {
            banner.setId(UUIDUtil.getUUID());
            banner.setCreateTime(System.currentTimeMillis());
            banner.setCreatorId(UserUtil.getUserId());
        } else {
            Banner old = repository.findOne(banner.getId());
            banner.setCreateTime(old.getCreateTime());
            banner.setCreatorId(old.getCreatorId());
        }
        banner.setUpdateTime(System.currentTimeMillis());
        repository.save(banner);
        return banner;
    }

    @Override
    public Banner findOne(String id) {
        return repository.findOne(id);
    }

    @Override
    public void del(String id) {
        repository.delete(id);
    }

    @Override
    public List<Banner> homeList() {
        Query query = new Query();
        query.with(new Sort(Sort.Direction.ASC, ModelConstant.BANNER_NUMBER));
        return template.find(query, Banner.class);
    }
}
