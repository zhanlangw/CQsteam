package cn.bytecloud.steam.event.dto;

import cn.bytecloud.steam.event.entity.Event;
import cn.bytecloud.steam.util.EmptyUtil;
import cn.bytecloud.steam.util.StringUtil;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Objects;


@Data
public class AddEventDto {
    @NotEmpty
    private String address;

    @NotEmpty
    private String stage;

    @NotEmpty
    private String title;

    @NotEmpty
    private String startTime;

    @NotEmpty
    private String endTime;

    public Long getStartTime() {
        if (EmptyUtil.isEmpty(startTime)) {
            return null;
        }
        return Objects.requireNonNull(StringUtil.getTime(startTime)).getTime();
    }

    public Long getEndTime() {
        if (EmptyUtil.isEmpty(endTime)) {
            return null;
        }
        return Objects.requireNonNull(StringUtil.getTime(endTime)).getTime();
    }

    public Event toData() {
        Event event = new Event();
        event.setTitle(title);
        event.setAddress(address);
        event.setStage(stage);
        event.setStartTime(getStartTime());
        event.setEndTime(getEndTime());
        return event;
    }
}
