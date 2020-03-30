package cn.bytecloud.steam.project.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import static cn.bytecloud.steam.constant.ModelConstant.*;

@Data
public class Teacher {

    //编号
    @Field(PROJECT_TEACHER_LEVLE)
    private Integer number;

    //名字
    @Field(PROJECT_TEACHER_NAME)
    private String name;

    //性别
    @Field(PROJECT_TEACHER_GENDER)
    private String gender;

    //学科
    @Field(PROJECT_TEACHER_SUBJECT)
    private String subject;

    //电话
    @Field(PROJECT_TEACHER_TELEPHONE)
    private String telephone;
}
