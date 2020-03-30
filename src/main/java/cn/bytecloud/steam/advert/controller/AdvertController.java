package cn.bytecloud.steam.advert.controller;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.advert.dto.AddAdvertDto;
import cn.bytecloud.steam.advert.dto.AdvertDto;
import cn.bytecloud.steam.advert.service.AdvertService;
import cn.bytecloud.steam.annotation.Menu;
import cn.bytecloud.steam.annotation.Permission;
import cn.bytecloud.steam.base.dto.APIResult;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Menu("广告")
public class AdvertController {

    public static final String API = "/api/advert/";

    @Autowired
    private AdvertService service;

    @GetMapping(API + "list")
    @RequiresPermissions({API + "list"})
    @Permission("列表")
    public APIResult list() {
        return APIResult.success().setValue(service.list());
    }

    @PostMapping(API + "upd")
    @RequiresPermissions({API + "upd"})
    @Permission("修改")
    public APIResult upd(@Validated @RequestBody AdvertDto dto) {
        service.upd(dto);
        return APIResult.success();
    }

    @PostMapping(API + "add")
    @RequiresPermissions({API + "add"})
    @Permission("添加")
    public APIResult add(@Validated @RequestBody AddAdvertDto dto) {
        service.add(dto);
        return APIResult.success();
    }

    @GetMapping(API + "del")
    @RequiresPermissions({API + "del"})
    @Permission("删除广告")
    public APIResult add(@RequestParam String id) throws ByteException {
        service.del(id);
        return APIResult.success();
    }

    @GetMapping(API + "item")
    @RequiresPermissions({API + "item"})
    @Permission("详情")
    public APIResult item(@RequestParam String id) {
        return APIResult.success().setValue(service.item(id));
    }

    @GetMapping("/api/home/advert")
    public APIResult showAdvert(@RequestParam Integer type) {
        return APIResult.success().setValue(service.showAdvert(type));
    }
}
