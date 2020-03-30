package cn.bytecloud.steam.category.entity;

import cn.bytecloud.steam.util.BaseEnum;

public enum StageType implements BaseEnum {
    PRELIMINARY(1, "初赛"), REMATCH(2, "复赛"), FINALS(3, "决赛");

    private String value;

    private Integer type;

    StageType(int type, final String value) {
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

    public static StageType getType(int type) {
        return type == 1 ? PRELIMINARY : (type == 2 ? REMATCH : FINALS);
    }

    public static StageType findByName(String stage) throws Exception {
        switch (stage) {
            case "初赛":
                return PRELIMINARY;
            case "复赛":
                return REMATCH;
            case "决赛":
                return FINALS;
                default:
                    throw new Exception();
        }
    }
}
