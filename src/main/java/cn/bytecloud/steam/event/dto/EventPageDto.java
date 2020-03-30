package cn.bytecloud.steam.event.dto;

import cn.bytecloud.steam.base.dto.BasePageDto;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import static cn.bytecloud.steam.constant.ModelConstant.EVENT_ADDRESS;
import static cn.bytecloud.steam.constant.ModelConstant.EVENT_STAGE;
import static cn.bytecloud.steam.constant.ModelConstant.EVENT_TITLE;

@Data
public class EventPageDto extends BasePageDto {
    private String address;

    private String stage;

    private String title;

}
