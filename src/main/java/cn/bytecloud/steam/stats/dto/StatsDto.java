package cn.bytecloud.steam.stats.dto;

import cn.bytecloud.steam.category.entity.GroupType;
import cn.bytecloud.steam.util.EmptyUtil;
import cn.bytecloud.steam.util.StringUtil;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Objects;
import java.util.Optional;

@Data
public class StatsDto {

    private String categoryId;

    private String areaId;

    private Integer group;

    private String schoolId;

    private String gender;

    @NotEmpty
    private String startTime;

    @NotEmpty
    private String endTime;

    public Optional<GroupType> getGroup() {
        return Optional.ofNullable(GroupType.getType(group));
    }

    public Long getStartTime() {

        if (EmptyUtil.isEmpty(startTime)) {
            return null;
        }
        return Objects.requireNonNull(StringUtil.getTime(startTime+" 00:00:00")).getTime();
    }

    public Long getEndTime() {
        if (EmptyUtil.isEmpty(endTime)) {
            return null;
        }
        return Objects.requireNonNull(StringUtil.getTime(endTime+" 23:59:59")).getTime();
    }
}
