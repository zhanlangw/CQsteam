package cn.bytecloud.steam.advert.service;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.exception.ErrorCode;
import cn.bytecloud.steam.advert.dao.AdvertDao;
import cn.bytecloud.steam.advert.dao.AdvertRepository;
import cn.bytecloud.steam.advert.dto.AddAdvertDto;
import cn.bytecloud.steam.advert.dto.AdvertDto;
import cn.bytecloud.steam.advert.entity.Advert;
import cn.bytecloud.steam.advert.entity.AdvertType;
import cn.bytecloud.steam.file.service.FileService;
import cn.bytecloud.steam.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdvertServiceImpl implements AdvertService {
    @Autowired
    private AdvertDao dao;

    @Autowired
    private AdvertRepository repository;

    @Autowired
    private FileService fileService;

    @Override
    public Object list() {
        return dao.findAll().stream().map(item -> item.toDto()).collect(Collectors.toList());
    }

    @Override
    public void upd(AdvertDto dto) {
        dao.upd(dto.toData());
    }

    @Override
    public Object item(String id) {
        return repository.findOne(id).toDto();
    }

    @Override
    public Map add(AddAdvertDto dto) {
        Advert advert = dto.toData();
        advert.setCreateTime(System.currentTimeMillis());
        advert.setType(AdvertType.HOME);
        advert.setId(UUIDUtil.getUUID());
        return repository.save(advert).toDto();
    }

    @Override
    public Object showAdvert(Integer type) {
        return repository.findByType(AdvertType.getType(type)).stream().map(Advert::toDto).collect(Collectors.toList());
    }

    @Override
    public void del(String id) throws ByteException {
        Advert advert = repository.findOne(id);
        if (advert.getType() != AdvertType.HOME) {
            throw new ByteException(ErrorCode.FAILURE, "只能删除首页广告");
        }
        fileService.deleteFile(advert.getImagePath());
        repository.delete(id);
    }
}
