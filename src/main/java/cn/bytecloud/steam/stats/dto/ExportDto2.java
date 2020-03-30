package cn.bytecloud.steam.stats.dto;

import cn.bytecloud.steam.category.entity.GroupType;
import cn.bytecloud.steam.util.EmptyUtil;
import cn.bytecloud.steam.util.StringUtil;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Objects;
import java.util.Optional;
@Data
public class ExportDto2 {
    private String categoryId2;

    private String areaId2;

    private Integer group2;

    private String schoolId2;

    private String gender2;

    @NotEmpty
    private String startTime;

    @NotEmpty
    private String endTime;

    public StatsDto toDto(){
        StatsDto dto = new StatsDto();
        dto.setCategoryId(categoryId2);
        dto.setAreaId(areaId2);
        dto.setGender(gender2);
        dto.setSchoolId(schoolId2);
        dto.setEndTime(endTime);
        dto.setStartTime(startTime);
        dto.setGender(gender2);
        return dto;
    }
}
