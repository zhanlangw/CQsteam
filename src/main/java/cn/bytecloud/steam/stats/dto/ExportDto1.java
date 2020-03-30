package cn.bytecloud.steam.stats.dto;

import cn.bytecloud.steam.category.entity.GroupType;
import cn.bytecloud.steam.util.EmptyUtil;
import cn.bytecloud.steam.util.StringUtil;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Objects;
import java.util.Optional;

@Data
public class ExportDto1 {
    private String categoryId1;

    private String areaId1;

    private Integer group1;

    private String schoolId1;

    private String gender1;

    @NotEmpty
    private String startTime;

    @NotEmpty
    private String endTime;

    public StatsDto toDto(){
        StatsDto dto = new StatsDto();
        dto.setCategoryId(categoryId1);
        dto.setAreaId(areaId1);
        dto.setGender(gender1);
        dto.setSchoolId(schoolId1);
        dto.setEndTime(endTime);
        dto.setStartTime(startTime);
        dto.setGender(gender1);
        return dto;
    }
}
