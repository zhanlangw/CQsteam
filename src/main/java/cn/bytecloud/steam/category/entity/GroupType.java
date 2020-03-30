package cn.bytecloud.steam.category.entity;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.exception.ErrorCode;
import cn.bytecloud.steam.util.BaseEnum;

public enum GroupType implements BaseEnum {
    PRIMARY_SCHOOL(1, "小学"), JUNIOR_HIGH_SCHOOL(2, "初中"), HIGH_SCHOOL(3, "高中"), MIDDLE_SCHOOL(4, "中学");

    private String value;

    private Integer type;

    GroupType(int type, final String value) {
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

    public static GroupType getType(Integer type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case 1:
                return PRIMARY_SCHOOL;
            case 2:
                return JUNIOR_HIGH_SCHOOL;
            case 3:
                return HIGH_SCHOOL;
            case 4:
                return MIDDLE_SCHOOL;
            default:
                return null;
        }
    }

    public static GroupType getByName(String name) throws ByteException {

        if ("小学".equals(name)) {
            return PRIMARY_SCHOOL;
        } else if ("初中".equals(name)) {
            return JUNIOR_HIGH_SCHOOL;
        } else if ("高中".equals(name)) {
            return HIGH_SCHOOL;
        } else if ("中学".equals(name)) {
            return MIDDLE_SCHOOL;
        }else {
            throw new ByteException(ErrorCode.FAILURE);
        }
    }
}
