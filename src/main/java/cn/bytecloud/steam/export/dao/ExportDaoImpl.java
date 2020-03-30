package cn.bytecloud.steam.export.dao;

import cn.bytecloud.steam.base.dao.BaseDao;
import cn.bytecloud.steam.base.dto.PageModel;
import cn.bytecloud.steam.constant.ModelConstant;
import cn.bytecloud.steam.export.dto.ExportPageDto;
import cn.bytecloud.steam.export.entity.Export;
import cn.bytecloud.steam.util.EmptyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


@Service
public class ExportDaoImpl extends BaseDao<Export> implements ExportDao {
    @Autowired
    private ExportRepository repository;

    @Autowired
    private MongoTemplate template;

    @Override
    public PageModel<Export> list(ExportPageDto dto) {
        Query query = new Query();
        if (EmptyUtil.isNotEmpty(dto.getName())) {
            query.addCriteria(Criteria.where(ModelConstant.EXPORT_NAME).regex(dto.getName()));
        }
        if (dto.getStatus() != null) {
            query.addCriteria(Criteria.where(ModelConstant.EXPORT_STATUS).is(dto.getStatus()));
        }
        query.with(new Sort(Sort.Direction.DESC, ModelConstant.CREATE_TIME));
        return pageList(query, dto, ModelConstant.CREATE_TIME);

    }
}
