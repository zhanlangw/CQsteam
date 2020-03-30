package cn.bytecloud.steam.export.dto;

import cn.bytecloud.steam.export.entity.Export;
import cn.bytecloud.steam.export.entity.ExportStatus;
import cn.bytecloud.steam.util.UUIDUtil;
import cn.bytecloud.steam.util.UserUtil;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class AddExportDto {
    @NotNull
    @Max(2)
    @Min(1)
    private Integer type;

//    @NotEmpty
//    @Length(max = 40)
    private String name;

    public Export toData() {
        Export export = new Export();
        export.setName(name);
        export.setId(UUIDUtil.getUUID());
        export.setStatus(ExportStatus.EXPORTING);
        export.setCreatorId(UserUtil.getUserId());
        export.setCreateTime(System.currentTimeMillis());
        return export;
    }
}
