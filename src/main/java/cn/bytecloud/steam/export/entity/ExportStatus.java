package cn.bytecloud.steam.export.entity;

import cn.bytecloud.steam.util.BaseEnum;

public enum ExportStatus implements BaseEnum {
    EXPORTING(1, "正在导出"), SUCCESS(2, "导出成功"), FAILURE(3, "导出失败");

    private Integer type;

    private String value;

    ExportStatus(int type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public Integer getEnumType() {
        return type;
    }

    @Override
    public String getEnumValue() {
        return value;
    }

    public static ExportStatus getExportStatus(int status) {
        switch (status) {
            case 1:
                return EXPORTING;
            case 2:
                return SUCCESS;
            default:
                return FAILURE;
        }
    }
}
