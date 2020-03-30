package cn.bytecloud.steam.project.dto;

import cn.bytecloud.steam.category.entity.Category;
import cn.bytecloud.steam.project.entity.Teacher;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

import static cn.bytecloud.steam.category.entity.ParameterSetting.SUBJECT;

@Data
public class TeacherDto {
    @NotNull
    private Integer number;

    @NotEmpty
    private String name;

    @NotEmpty
    private String gender;

    private String subject;

    @NotEmpty
    private String telephone;

    public Teacher toData(Category category) {
        Teacher teacher = new Teacher();
        teacher.setNumber(number);
        teacher.setName(name);
        teacher.setGender(gender);
        if (category.getParamSetting().contains(SUBJECT)) {
            teacher.setSubject(subject);
        }
        teacher.setTelephone(telephone);
        return teacher;
    }
}
