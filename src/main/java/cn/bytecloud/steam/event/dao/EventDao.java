package cn.bytecloud.steam.event.dao;

import cn.bytecloud.steam.base.dto.PageModel;
import cn.bytecloud.steam.event.dto.EventPageDto;
import cn.bytecloud.steam.event.entity.Event;

import java.util.HashMap;

public interface EventDao {
    Event save(Event event);

    PageModel<HashMap> list(EventPageDto dto);

    Object homeList();

}
