package cn.bytecloud.steam.result.service;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.file.service.FileService;
import cn.bytecloud.steam.result.dao.ResultDao;
import cn.bytecloud.steam.result.dao.ResultRepository;
import cn.bytecloud.steam.result.dto.AddResultDto;
import cn.bytecloud.steam.result.dto.ResultPageDto;
import cn.bytecloud.steam.result.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResultServiceImpl implements ResultService {
    @Autowired
    private ResultDao dao;

    @Autowired
    private ResultRepository repository;

    @Autowired
    private FileService fileService;

    @Override
    public Object save(AddResultDto dto) {
        return dao.save(dto.toData()).toDto();
    }

    @Override
    public void del(String id) throws ByteException {
        Result result = repository.findOne(id);
        fileService.deleteFile(result.getPath());
        repository.delete(id);
    }

    @Override
    public Object list(ResultPageDto dto) {
        return dao.list(dto);
    }

    @Override
    public Object item(String id) {
        return repository.findOne(id).toDto();
    }
}
