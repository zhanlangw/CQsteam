package cn.bytecloud.steam.category.service;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.category.dto.AddCategoryDto;
import cn.bytecloud.steam.category.dto.CategoryPageDto;
import cn.bytecloud.steam.category.dto.UpdCategoryDto;
import cn.bytecloud.steam.category.entity.Category;

import java.util.Map;

public interface CategoryService {
    Map<String, Object> save(AddCategoryDto dto) throws ByteException;

    Object item(String id);

    Map<String, Object> upd(UpdCategoryDto dto) throws ByteException;

    Object list(CategoryPageDto pageDto, boolean b);

    void del(String id) throws ByteException;

    Category findOne(String categoryId);

    Object stage(String id);
}
