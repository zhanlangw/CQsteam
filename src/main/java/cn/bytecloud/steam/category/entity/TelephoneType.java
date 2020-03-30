package cn.bytecloud.steam.category.entity;

import cn.bytecloud.steam.util.BaseEnum;

public enum TelephoneType implements BaseEnum {
    PARENT_TELEPHONE(1, "家长手机航母"), TELEPHONE(2, "手机号码");

    private Integer type;
    private String value;

    TelephoneType(int type, final String value) {
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
