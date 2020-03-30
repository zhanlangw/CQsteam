package cn.bytecloud.steam.school.dto;

import cn.bytecloud.steam.base.dto.BasePageDto;
import cn.bytecloud.steam.category.entity.GroupType;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
public class SchoolPageDto extends BasePageDto {
    @NotEmpty
    private String areaId;

    private String name;

    @NotNull
    private Integer group;

    public GroupType getGroup() {
        return GroupType.getType(group);
    }
}
