package cn.bytecloud.steam.area.dao;

import cn.bytecloud.steam.area.entity.Area;

public interface AreaDao {
    Area findOme(String areaId);

    Object list(String areaIds);
}
