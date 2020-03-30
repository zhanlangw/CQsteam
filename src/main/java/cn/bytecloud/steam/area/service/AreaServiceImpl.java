package cn.bytecloud.steam.area.service;

import cn.bytecloud.steam.area.dao.AreaDao;
import cn.bytecloud.steam.area.dao.AreaRepository;
import cn.bytecloud.steam.area.dto.City;
import cn.bytecloud.steam.area.dto.Province;
import cn.bytecloud.steam.area.entity.Area;
import cn.bytecloud.steam.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AreaServiceImpl implements AreaService {
    @Autowired
    private AreaDao dao;
    @Autowired
    private AreaRepository repository;

    @Override
    public Area findOne(final String areaId) {
        return dao.findOme(areaId);
    }

    @Override
    public void importData(List<Province> provinceList) {
        //删除所有数据
        List<Area> areaList = repository.findAll();
        for (Area area : areaList) {
            repository.delete(area.getId());
        }
        for (Province province : provinceList) {
            //保存省
            Area tProvince = new Area();
            String provinceId = UUIDUtil.getUUID();

            province.setId(provinceId);
            String provinceName = province.getName();
            if (provinceName.endsWith("省") || provinceName.endsWith("市")) {
                String substring = provinceName.substring(0, provinceName.length() - 1);
                province.setName(substring);
            }
            tProvince.setId(provinceId);
            tProvince.setName(province.getName());

            repository.save(tProvince);

            ArrayList<City> cityList = province.getCity();
            for (City city : cityList) {
                //保存市
                String cityId = UUIDUtil.getUUID();

                Area tCity = new Area();
                tCity.setId(cityId);
                tCity.setParentId(provinceId);
                tCity.setName(city.getName());

                String[] areas = city.getArea();
                repository.save(tCity);

                for (int i = 0; i < areas.length; i++) {

                    //保存区县
                    Area area = new Area();
                    area.setName(areas[i]);
                    area.setId(UUIDUtil.getUUID());
                    area.setParentId(cityId);
                    repository.save(area);
                }
            }
        }
    }


    @Override
    public Object tree(String id) {
        List<Map> list = new ArrayList<>();
        repository.findByParentId("*".equals(id) ? null : id).forEach(item -> {
            Map map = new HashMap();
            map.put("key", item.getId());
            map.put("title", item.getName());
            map.put("uid", item.getParentId());
            map.put("isLeaf", repository.findByParentId(item.getId()).size() > 0 ? false : true);
            list.add(map);
        });
        return list;
    }

    @Override
    public Object list(String areaIds) {
        return dao.list(areaIds);
    }

    @Override
    public List<Area> findByParentId(String id) {
        return repository.findByParentId(id);
    }

    @Override
    public List<Area> getExportArea() {
        return repository.findByParentId(repository.findByName("重庆市").getId());
    }
}
