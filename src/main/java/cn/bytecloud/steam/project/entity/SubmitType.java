package cn.bytecloud.steam.project.entity;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.exception.ErrorCode;
import cn.bytecloud.steam.util.BaseEnum;

public enum SubmitType implements BaseEnum {
    BASIS_MSG(1, "基础信息"), PROJECT_BASIS_MSG(2, "项目基础信息"), TEACHER_MSG(3, "指导老师"), PRELIMINARY(4, "初赛"), REMATCH(5,
            "复赛"), FINALS(6, "决赛"), END(7, "结束");

    private Integer type;
    private String value;

    SubmitType(int type, final String value) {
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

    public static SubmitType getType(int type) throws ByteException {
        switch (type) {
            case 1:
                return BASIS_MSG;
            case 2:
                return PROJECT_BASIS_MSG;
            case 3:
                return TEACHER_MSG;
            case 4:
                return PRELIMINARY;
            case 5:
                return REMATCH;
            case 6:
                return FINALS;
            case 7:
                return END;
            default:
                throw new ByteException(ErrorCode.FAILURE);
        }
    }
}
