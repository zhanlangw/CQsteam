package cn.bytecloud.steam.user.entity;

import cn.bytecloud.steam.util.BaseEnum;

public enum UserType implements BaseEnum {
    USER(1, "普通用户"), ADMIN(2, "管理员"), ROOT(3, "超级管理员");

    private Integer type;
    private String value;

    UserType(int type, final String value) {
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
