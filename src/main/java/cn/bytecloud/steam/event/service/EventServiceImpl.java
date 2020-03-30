package cn.bytecloud.steam.event.service;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.exception.ErrorCode;
import cn.bytecloud.steam.event.dao.EventDao;
import cn.bytecloud.steam.event.dao.EventRepository;
import cn.bytecloud.steam.event.dto.AddEventDto;
import cn.bytecloud.steam.event.dto.EventPageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {
    @Autowired
    private EventDao dao;

    @Autowired
    private EventRepository repository;

    @Override
    public Object save(AddEventDto dto) throws ByteException {
        if (dto.getStartTime() >= dto.getEndTime()) {
            throw new ByteException(ErrorCode.PARAMETER);
        }
        return dao.save(dto.toData()).toDto();
    }

    @Override
    public Object item(String id) {
        return repository.findOne(id).toDto();
    }

    @Override
    public void del(String id) {
        repository.delete(id);
    }

    @Override
    public Object list(EventPageDto dto) {
        return dao.list(dto);
    }

    @Override
    public Object homeList() {
        return dao.homeList();
    }
}
