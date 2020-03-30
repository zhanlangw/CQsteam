package cn.bytecloud.steam.school.dto;

import cn.bytecloud.steam.category.entity.GroupType;
import cn.bytecloud.steam.school.entity.School;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
public class AddSchoolDto {
    @NotEmpty
    private String areaId;
    @NotEmpty
    private String name;

    @NotNull
    private Integer group;

    public School toData() {
        School school = new School();
        school.setName(name);
        school.setGroup(GroupType.getType(group));
        school.setAreaId(areaId);
        return school;
    }
}
