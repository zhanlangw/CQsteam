package cn.bytecloud.steam.video.service;

import cn.bytecloud.steam.video.dao.VideoDao;
import cn.bytecloud.steam.video.dao.VideoRepository;
import cn.bytecloud.steam.video.dto.AddVideoDto;
import cn.bytecloud.steam.video.dto.VideoPageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoServiceImpl implements VideoService {
    @Autowired
    private VideoDao dao;

    @Autowired
    private VideoRepository repository;

    @Override
    public Object sava(AddVideoDto dto) {
        return dao.save(dto.toData()).toDto();
    }

    @Override
    public void del(String id) {
        repository.delete(id);
    }

    @Override
    public Object list(VideoPageDto dto) {
        return dao.list(dto);
    }

    @Override
    public Object item(String id) {
        return repository.findOne(id).toDto();
    }
}
