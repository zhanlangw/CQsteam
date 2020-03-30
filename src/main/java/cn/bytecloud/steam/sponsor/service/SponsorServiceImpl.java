package cn.bytecloud.steam.sponsor.service;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.file.service.FileService;
import cn.bytecloud.steam.sponsor.dao.SponsorDao;
import cn.bytecloud.steam.sponsor.dao.SponsorRepository;
import cn.bytecloud.steam.sponsor.dto.AddSponsorDto;
import cn.bytecloud.steam.sponsor.dto.SponsorPageDto;
import cn.bytecloud.steam.sponsor.entity.Sponsor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SponsorServiceImpl implements VideoService {
    @Autowired
    private SponsorDao dao;

    @Autowired
    private SponsorRepository repository;

    @Autowired
    private FileService fileService;
    @Override
    public Object sava(AddSponsorDto dto) {
        return dao.save(dto.toData()).toDto();
    }

    @Override
    public void del(String id) throws ByteException {
        Sponsor sponsor = repository.findOne(id);
        fileService.deleteFile(sponsor.getPath());
        repository.delete(id);
    }

    @Override
    public Object list(SponsorPageDto dto) {
        return dao.list(dto);
    }

    @Override
    public Object item(String id) {
        return repository.findOne(id).toDto();
    }
}
