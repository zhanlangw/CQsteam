package cn.bytecloud.steam.category.entity;

import cn.bytecloud.steam.util.BaseEnum;

public enum CategoryStatus implements BaseEnum {
    NOT_START(1, "未开始"), PRELIMINARY(2, "初赛"), REMATCH(3, "复赛"), FINALS(4, "决赛"), END(5, "结束");

    private Integer type;
    private String value;

    CategoryStatus(int type, final String value) {
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

    public static CategoryStatus getType(int type) {
        switch (type) {
            case 1:
                return NOT_START;
            case 2:
                return PRELIMINARY;
            case 3:
                return REMATCH;
            case 4:
                return FINALS;
            case 5:
                return END;
            default:
                return null;
        }
    }
}
