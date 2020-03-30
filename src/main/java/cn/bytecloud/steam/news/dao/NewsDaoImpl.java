package cn.bytecloud.steam.news.dao;

import cn.bytecloud.steam.base.dao.BaseDao;
import cn.bytecloud.steam.base.dto.BasePageDto;
import cn.bytecloud.steam.base.dto.PageModel;
import cn.bytecloud.steam.constant.ModelConstant;
import cn.bytecloud.steam.news.dto.NewsPageDto;
import cn.bytecloud.steam.news.entity.News;
import cn.bytecloud.steam.util.EmptyUtil;
import cn.bytecloud.steam.util.UUIDUtil;
import cn.bytecloud.steam.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static cn.bytecloud.steam.constant.ModelConstant.*;

@Repository
public class NewsDaoImpl extends BaseDao<News> implements NewsDao {
    @Autowired
    private MongoTemplate template;

    @Autowired
    private NewsRepository repository;

    @Override
    public News save(News news) {
        if (EmptyUtil.isEmpty(news.getId())) {
            news.setId(UUIDUtil.getUUID());
            news.setCreatorId(UserUtil.getUserId());
            news.setCreateTime(System.currentTimeMillis());
        } else {
            News old = repository.findOne(news.getId());
            news.setCreatorId(old.getCreatorId());
            news.setCreateTime(old.getCreateTime());
        }
        news.setUpdateTime(System.currentTimeMillis());
        repository.save(news);
        return news;
    }

    @Override
    public Object list(NewsPageDto dto) {
        Query query = new Query();
        query.addCriteria(new Criteria().orOperator(Criteria.where("").is(""), Criteria.where("").is("")));
        query.addCriteria(Criteria.where("").elemMatch(new Criteria().in("arr")));
        List<AggregationOperation> list = addMatch(dto, CREATE_TIME);

        if (EmptyUtil.isNotEmpty(dto.getTitle())) {
            list.add(Aggregation.match(Criteria.where(NEWS_TITLE).regex(dto.getTitle())));
        }

        list.add(LookupOperation.newLookup()
                .from(T_USER)
                .localField(CREATOR_ID)
                .foreignField(ID)
                .as("user")
        );
        if (EmptyUtil.isNotEmpty(dto.getCreator())) {
            list.add(Aggregation.match(Criteria.where("user." + USER_NAME).regex(dto.getCreator())));
        }
        list.add(Aggregation.sort(new Sort(Sort.Direction.DESC, CREATE_TIME)));

        list.add(Aggregation.project()
                .and(NEWS_TITLE).as("title")
                .and("user." + USER_NAME).as("creator")
                .and(CREATE_TIME).as("createTime")
                .and(ID).as("id")
                .andExclude(ID)
        );

        return pageList(list, dto, T_NEWS, CREATE_TIME);
    }

    @Override
    public PageModel<News> homeList(BasePageDto dto) {
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC, ModelConstant.CREATE_TIME));
        return pageList(query, dto, null);
    }
}
