package cn.bytecloud.steam.category.dao;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.base.dto.PageModel;
import cn.bytecloud.steam.category.dto.CategoryPageDto;
import cn.bytecloud.steam.category.entity.Category;

import java.util.List;

public interface CategoryDao {
    Category save(Category category) throws ByteException;

    List<Category> findByNameOrAbbreviation(String name, String abbreviation);

    List<Category> findByName(String name);

    List<Category> findByAbbreviation(String abbreviation);

    Category findById(String id);

    PageModel<Category> list(CategoryPageDto pageDto, boolean timeFlag);

    void del(String id);
}
