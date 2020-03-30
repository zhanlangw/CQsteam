package cn.bytecloud.steam.event.dto;

import cn.bytecloud.steam.event.entity.Event;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class UpdEventDto extends AddEventDto {
    @NotEmpty
    private String id;

    @Override
    public Event toData() {
        Event event = super.toData();
        event.setId(id);
        return event;
    }
}
