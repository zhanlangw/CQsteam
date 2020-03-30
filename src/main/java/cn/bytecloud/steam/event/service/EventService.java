package cn.bytecloud.steam.event.service;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.event.dto.AddEventDto;
import cn.bytecloud.steam.event.dto.EventPageDto;

public interface EventService {
    Object save(AddEventDto dto) throws ByteException;

    Object item(String id);

    void del(String id);

    Object list(EventPageDto dto);

    Object homeList();

}
