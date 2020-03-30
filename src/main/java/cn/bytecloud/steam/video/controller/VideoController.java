package cn.bytecloud.steam.video.controller;

import cn.bytecloud.steam.annotation.Menu;
import cn.bytecloud.steam.annotation.Permission;
import cn.bytecloud.steam.base.dto.APIResult;
import cn.bytecloud.steam.video.dto.AddVideoDto;
import cn.bytecloud.steam.video.dto.UpdVideoDto;
import cn.bytecloud.steam.video.dto.VideoPageDto;
import cn.bytecloud.steam.video.service.VideoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Menu("视屏专区")
public class VideoController {

    public static final String API = "/api/video/";

    @Autowired
    private VideoService service;

    @PostMapping(API + "add")
    @RequiresPermissions({API + "add"})
    @Permission("添加")
    public APIResult add(@RequestBody @Validated AddVideoDto dto) {
        return APIResult.success().setValue(service.sava(dto));
    }

    @PostMapping(API + "upd")
    @RequiresPermissions({API + "upd"})
    @Permission("修改")
    public APIResult upd(@RequestBody @Validated UpdVideoDto dto) {
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
    public APIResult del(@RequestParam String id) {
        service.del(id);
        return APIResult.success();
    }

    @GetMapping(API + "list")
    @RequiresPermissions({API + "list"})
    @Permission("列表")
    public APIResult list(@Validated VideoPageDto dto) {
        return APIResult.success().setValue(service.list(dto));
    }

    @GetMapping("/api/home/video/list")
    public APIResult listHome(@Validated VideoPageDto dto) {
        return APIResult.success().setValue(service.list(dto));
    }
}
