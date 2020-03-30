package cn.bytecloud.steam.banner.service;

import cn.bytecloud.steam.banner.dao.BannerDao;
import cn.bytecloud.steam.banner.dao.BannerRepository;
import cn.bytecloud.steam.banner.dto.AddBannerDto;
import cn.bytecloud.steam.banner.dto.BannerPageDto;
import cn.bytecloud.steam.banner.entity.Banner;
import cn.bytecloud.steam.base.dao.BaseDao;
import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.exception.ErrorCode;
import cn.bytecloud.steam.file.service.FileService;
import cn.bytecloud.steam.util.EmptyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.bytecloud.steam.constant.ModelConstant.*;

@Service
public class BannerServiceImpl extends BaseDao<Banner> implements BannerService {
    @Autowired
    private BannerDao dao;

    @Autowired
    private BannerRepository repository;

    @Autowired
    private FileService fileService;

    @Override
    public Map save(AddBannerDto dto) throws ByteException {
        if (repository.findAll().size() > 8) {
            throw new ByteException(ErrorCode.FAILURE, "banner数量不能超过8个");
        }
        return item(dao.save(dto.toData()).getId());
    }

    @Override
    public void del(String id) throws ByteException {
        Banner banner = dao.findOne(id);
        fileService.deleteFile(banner.getBigImagePath());
        fileService.deleteFile(banner.getSmallImagePath());
        dao.del(id);
    }

    @Override
    public Map item(String id) {
        return dao.findOne(id).toDto();
    }

    @Override
    public Object list(BannerPageDto dto) {
        List<AggregationOperation> list = addMatch(dto, UPDATE_TIME);
        
        if (EmptyUtil.isNotEmpty(dto.getTitle())) {
            list.add(Aggregation.match(Criteria.where(BANNER_TITLE).regex(dto.getTitle())));
        }

        list.add(LookupOperation.newLookup()
                .from(T_USER)
                .localField(CREATOR_ID)
                .foreignField(ID)
                .as("user")
        );

        if (EmptyUtil.isNotEmpty(dto.getCreator())) {
            list.add(Aggregation.match(Criteria.where("user." + USER_NAME).regex(dto.getCreator())));
        }
        list.add(Aggregation.sort(new Sort(Sort.Direction.ASC, BANNER_NUMBER)));

        list.add(Aggregation.project()
                .and(BANNER_TITLE).as("title")
                .and(BANNER_NUMBER).as("number")
                .and(BANNER_BIG_IMAGE_PAHT).as("bigImagePath")
                .and(BANNER_SMALL_IMAGE_PAHT).as("smallImagePath")
                .and("user." + USER_NAME).as("creator")
                .and(UPDATE_TIME).as("updateTime")
                .and(ID).as("id")
                .andExclude(ID)
        );

        return pageList(list, dto, T_BANNER, UPDATE_TIME);
    }

    @Override
    public Object homeList() {
        List list = new ArrayList();
        dao.homeList().forEach(item -> {
            Map map = new HashMap();
            map.put("title", item.getTitle());
            map.put("url", item.getUrl());
            map.put("type", item.getType());
            map.put("bigImagePath", item.getBigImagePath());
            map.put("smallImagePath", item.getSmallImagePath());
            list.add(map);
        });
        return list;
    }
}
