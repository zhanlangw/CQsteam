package cn.bytecloud.steam.banner.controller;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.annotation.Menu;
import cn.bytecloud.steam.annotation.Permission;
import cn.bytecloud.steam.banner.dto.AddBannerDto;
import cn.bytecloud.steam.banner.dto.BannerPageDto;
import cn.bytecloud.steam.banner.dto.UpdBannerDto;
import cn.bytecloud.steam.banner.service.BannerService;
import cn.bytecloud.steam.base.dto.APIResult;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Menu("banner")
public class BannerController {
    @Autowired
    private BannerService service;

    public static final String API = "/api/banner/";

    @PostMapping(API + "add")
    @RequiresPermissions({API + "add"})
    @Permission("添加")
    public APIResult add(@Validated @RequestBody AddBannerDto dto) throws ByteException {
        return APIResult.success().setValue(service.save(dto));
    }

    @PostMapping(API + "upd")
    @RequiresPermissions({API + "upd"})
    @Permission("修改")
    public APIResult upd(@Validated @RequestBody UpdBannerDto dto) throws ByteException {
        return APIResult.success().setValue(service.save(dto));
    }

    @GetMapping(API + "del")
    @RequiresPermissions({API + "del"})
    @Permission("删除")
    public APIResult del(@RequestParam("id") String id) throws ByteException {
        service.del(id);
        return APIResult.success();
    }

    @GetMapping(API + "item")
    @RequiresPermissions({API + "item"})
    @Permission("详情")
    public APIResult item(@RequestParam String id) {
        return APIResult.success().setValue(service.item(id));
    }

    @GetMapping(API + "list")
    @RequiresPermissions({API + "list"})
    @Permission("列表")
    public APIResult list(@Valid BannerPageDto dto) {
        return APIResult.success().setValue(service.list(dto));
    }

    @GetMapping("/api/home/banner")
    public APIResult userList() {
        return APIResult.success().setValue(service.homeList());
    }
}
