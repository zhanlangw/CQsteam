package cn.bytecloud.steam.category.entity;

import cn.bytecloud.steam.util.BaseEnum;

public enum ParameterSetting implements BaseEnum {
    MEMBER_LDENTITY(1, "成员身份"), SUBJECT(2, "学科"), GUIDE_TEACHER(3, "指导老师"), NAME_AND_DESC(4, "名称和简介"), OTHER(5, "其他");


    private Integer type;
    private String value;

    ParameterSetting(int type, final String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String getEnumValue() {
        return value;
    }

    @Override
    public Integer getEnumType() {
        return type;
    }

}
