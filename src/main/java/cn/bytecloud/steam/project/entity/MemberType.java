package cn.bytecloud.steam.project.entity;

import cn.bytecloud.steam.util.BaseEnum;

public enum MemberType implements BaseEnum {
    LEADER(1, "队长"), MEMBER(2, "成员");

    private Integer type;
    private String value;

    MemberType(int type, final String value) {
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

    public static MemberType getType(int type) {
        return type == 1 ? LEADER : MEMBER;
    }
}
