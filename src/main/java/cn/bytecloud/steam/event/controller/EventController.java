package cn.bytecloud.steam.event.controller;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.annotation.Menu;
import cn.bytecloud.steam.annotation.Permission;
import cn.bytecloud.steam.base.dto.APIResult;
import cn.bytecloud.steam.event.dto.AddEventDto;
import cn.bytecloud.steam.event.dto.EventPageDto;
import cn.bytecloud.steam.event.dto.UpdEventDto;
import cn.bytecloud.steam.event.service.EventService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Menu("活动日历")
public class EventController {
    public static final String API = "/api/event/";

    @Autowired
    private EventService service;

    @PostMapping(API + "add")
    @RequiresPermissions({API + "add"})
    @Permission("添加")
    public APIResult add(@Validated @RequestBody AddEventDto dto) throws ByteException {
        return APIResult.success().setValue((service.save(dto)));
    }

    @PostMapping(API + "upd")
    @RequiresPermissions({API + "upd"})
    @Permission("修改")
    public APIResult upd(@Validated @RequestBody UpdEventDto dto) throws ByteException {
        return APIResult.success().setValue(service.save(dto));
    }

    @GetMapping(API + "item")
    @RequiresPermissions({API + "item"})
    @Permission("详情")
    public APIResult item(@RequestParam String id) throws ByteException {
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
    public APIResult list(@Valid EventPageDto dto) throws ByteException {
        return APIResult.success().setValue(service.list(dto));
    }

    @GetMapping("/api/home/event")
    public APIResult homeList() throws ByteException {
        return APIResult.success().setValue(service.homeList());
    }
}
