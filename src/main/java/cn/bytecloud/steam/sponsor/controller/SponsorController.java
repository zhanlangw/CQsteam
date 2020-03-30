package cn.bytecloud.steam.sponsor.controller;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.annotation.Menu;
import cn.bytecloud.steam.annotation.Permission;
import cn.bytecloud.steam.base.dto.APIResult;
import cn.bytecloud.steam.sponsor.dto.AddSponsorDto;
import cn.bytecloud.steam.sponsor.dto.SponsorPageDto;
import cn.bytecloud.steam.sponsor.dto.UpdSponsorDto;
import cn.bytecloud.steam.sponsor.service.VideoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Menu("赞助商")
public class SponsorController {

    public static final String API = "/api/sponsor/";

    @Autowired
    private VideoService service;

    @PostMapping(API + "add")
    @RequiresPermissions({API + "add"})
    @Permission("添加")
    public APIResult add(@RequestBody @Validated AddSponsorDto dto) {
        return APIResult.success().setValue(service.sava(dto));
    }

    @PostMapping(API + "upd")
    @RequiresPermissions({API + "upd"})
    @Permission("修改")
    public APIResult upd(@RequestBody @Validated UpdSponsorDto dto) {
        return APIResult.success().setValue(service.sava(dto));
    }

    @GetMapping(API + "item")
    @RequiresPermissions({API + "item"})
    @Permission("详情")
    public APIResult item(@RequestParam String id) {
        return APIResult.success().setValue(service.item(id));
    }

    @GetMapping(API + "del")
    @RequiresPermissions({API + "del"})
    @Permission("删除")
    public APIResult del(@RequestParam String id) throws ByteException {
        service.del(id);
        return APIResult.success();
    }

    @GetMapping(API + "list")
    @RequiresPermissions({API + "list"})
    @Permission("列表")
    public APIResult list(@Validated SponsorPageDto dto) {
        return APIResult.success().setValue(service.list(dto));
    }

    @GetMapping("/api/home/sponsor/list")
    public APIResult listHome(@Validated SponsorPageDto dto) {
        return APIResult.success().setValue(service.list(dto));
    }
}
