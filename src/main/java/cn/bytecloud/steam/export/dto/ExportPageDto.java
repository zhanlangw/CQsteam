package cn.bytecloud.steam.export.dto;

import cn.bytecloud.steam.base.dto.BasePageDto;
import cn.bytecloud.steam.export.entity.ExportStatus;
import lombok.Data;

@Data
public class ExportPageDto extends BasePageDto {
    private String name;


    private Integer status;


    public ExportStatus getStatus() {
        return status == null ? null : ExportStatus.getExportStatus(status);
    }
}
