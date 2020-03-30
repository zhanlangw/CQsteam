package cn.bytecloud.steam.area.service;

import cn.bytecloud.steam.area.dto.Province;
import cn.bytecloud.steam.area.entity.Area;

import java.util.List;

public interface AreaService {
    Area findOne(String areaId);

    void importData(List<Province> provinceList);

    Object tree(String id);

    Object list(String areaIds);

    List<Area> findByParentId(String id);

    List<Area> getExportArea();

}
