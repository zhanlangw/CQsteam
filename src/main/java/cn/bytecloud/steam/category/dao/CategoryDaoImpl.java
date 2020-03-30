package cn.bytecloud.steam.category.dao;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.exception.ErrorCode;
import cn.bytecloud.steam.base.dao.BaseDao;
import cn.bytecloud.steam.base.dto.PageModel;
import cn.bytecloud.steam.category.dto.CategoryPageDto;
import cn.bytecloud.steam.category.entity.Category;
import cn.bytecloud.steam.util.EmptyUtil;
import cn.bytecloud.steam.util.UUIDUtil;
import cn.bytecloud.steam.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static cn.bytecloud.steam.constant.ModelConstant.*;


@Service
public class CategoryDaoImpl extends BaseDao<Category> implements CategoryDao {
    @Autowired
    private MongoTemplate template;

    @Autowired
    private CategoryRepository repository;

    @Override
    public Category save(final Category category) throws ByteException {
        if (EmptyUtil.isEmpty(category.getId())) {
            category.setId(UUIDUtil.getUUID());
            category.setCreateTime(System.currentTimeMillis());
            category.setCreatorId(UserUtil.getUserId());
        } else {
            Category old = repository.findOne(category.getId());
            if (old == null) {
                throw new ByteException(ErrorCode.NOT_FOUND);
            }
            category.setCreateTime(old.getCreateTime());
            category.setCreatorId(old.getCreatorId());
        }
        category.setUpdateTime(System.currentTimeMillis());
        template.save(category);
        return category;
    }

    @Override
    public List<Category> findByNameOrAbbreviation(String name, String abbreviation) {
        return repository.findByNameOrAbbreviation(name, abbreviation);
    }

    @Override
    public List<Category> findByName(final String name) {
        return repository.findByName(name);
    }

    @Override
    public List<Category> findByAbbreviation(final String abbreviation) {
        return repository.findByAbbreviation(abbreviation);
    }

    @Override
    public Category findById(final String id) {
        return repository.findOne(id);
    }

    @Override
    public PageModel<Category> list(final CategoryPageDto pageDto, boolean timeFlag) {
        Query query = new Query();
        if (EmptyUtil.isNotEmpty(pageDto.getName())) {
            query.addCriteria(Criteria.where(CATEGORY_NAME).regex(pageDto.getName()));
        }
        if (timeFlag) {
            query.addCriteria(Criteria.where(CATEGORY_SIGN_UP_TIME).lte(System.currentTimeMillis()));
        }
        query.with(new Sort(Sort.Direction.DESC, UPDATE_TIME));
        PageModel<Category> page = pageList(query, pageDto, UPDATE_TIME);
        if (timeFlag) {
            List<Category> list = page.getValue().stream().filter(category -> category.getStages().get(0).getEndTime()
                    > System.currentTimeMillis()).collect(Collectors.toList());
            page.setValue(list);
        }
        return page;
    }

    @Override
    public void del(final String id) {
        repository.delete(id);
    }


    public static void main(String[] args) {
        double data = method(100, 0, 0);
        System.out.println("共经历距离+ ::"+data);
    }

    private static double method(double high, double data, int count) {
        if (count >= 10) {
            System.out.println("第十次反弹高度+ ::"+high);
            return data - high;
        } else {
            return data + method(high / 2, high + high / 2, ++count);
        }
    }
}
