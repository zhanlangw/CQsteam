package cn.bytecloud.steam.category.dto;

import cn.bytecloud.steam.category.entity.Category;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
public class UpdCategoryDto extends AddCategoryDto {
    @NotEmpty
    private String id;

    @NotNull
    private Integer status;

    @Override
    public Category toDate() {
        Category category = super.toDate();
        category.setId(id);
        return category;
    }
}
