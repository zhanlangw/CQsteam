package cn.bytecloud.steam.area.controller;

import cn.bytecloud.steam.area.dto.Province;
import cn.bytecloud.steam.area.service.AreaService;
import cn.bytecloud.steam.base.dto.APIResult;
import cn.bytecloud.steam.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class AreaController {

    public static final String API = "/api/area/";

    @Autowired
    private AreaService service;

    @PostMapping(API + "import_data")
    public APIResult importData(@RequestBody List<Province> provinceList) {
        service.importData(provinceList);
        return APIResult.success();
    }

    @GetMapping(API + "tree")
    public APIResult tree(String id) {
        return APIResult.success().setValue(service.tree(id));
    }

    @GetMapping(API + "list")
    public APIResult list(@RequestParam String areaIds) {
        return APIResult.success().setValue(service.list(areaIds));
    }
}
