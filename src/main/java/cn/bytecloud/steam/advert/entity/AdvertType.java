package cn.bytecloud.steam.advert.entity;

import cn.bytecloud.steam.util.BaseEnum;

public enum AdvertType implements BaseEnum {
    BASIS(1, "项目基础信息广告"), SUBMIT(2, "提交项目状态"), HOME(3, "首页");


    AdvertType(int type, String value) {
        this.type = type;
        this.value = value;
    }

    private Integer type;
    private String value;


    @Override
    public Integer getEnumType() {
        return type;
    }

    @Override
    public String getEnumValue() {
        return value;
    }

    public static AdvertType getType(int type) {
        return type == 1 ? BASIS : (type == 2 ? SUBMIT : HOME);
    }
}
