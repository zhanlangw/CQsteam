package cn.bytecloud.steam.competitionMsg.entity;

import cn.bytecloud.steam.util.BaseEnum;

public enum CompetitionType implements BaseEnum {
    INTRODUCTION(1, "简介"), NOTICE(2, "须知");
    private Integer type;
    private String value;

    CompetitionType(int type, final String value) {
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
