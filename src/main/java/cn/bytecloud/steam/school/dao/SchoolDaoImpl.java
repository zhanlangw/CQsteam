package cn.bytecloud.steam.school.dao;

import cn.bytecloud.steam.base.dao.BaseDao;
import cn.bytecloud.steam.base.dto.PageModel;
import cn.bytecloud.steam.constant.ModelConstant;
import cn.bytecloud.steam.school.dto.SchoolPageDto;
import cn.bytecloud.steam.school.entity.School;
import cn.bytecloud.steam.util.EmptyUtil;
import cn.bytecloud.steam.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class SchoolDaoImpl extends BaseDao<School> implements SchoolDao {
    @Autowired
    private MongoTemplate template;

    @Autowired
    private SchoolRepository repository;

    @Override
    public School save(School school) {
        school.setId(UUIDUtil.getUUID());
        school.setCreateTime(System.currentTimeMillis());
        template.save(school);
        return school;
    }

    @Override
    public PageModel<School> list(SchoolPageDto dto) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ModelConstant.SCHOOL_AREA_ID).regex(dto.getAreaId()));
        if (EmptyUtil.isNotEmpty(dto.getName())) {
            query.addCriteria(Criteria.where(ModelConstant.SCHOOL_NAME).regex(dto.getName()));
        }
        if (null != dto.getGroup()) {
            query.addCriteria(Criteria.where(ModelConstant.SCHOOL_GROUP).is(dto.getGroup()));
        }
        query.with(new Sort(Sort.Direction.ASC, ModelConstant.CREATE_TIME));
        return pageList(query, dto, ModelConstant.CREATE_TIME);
    }
}
