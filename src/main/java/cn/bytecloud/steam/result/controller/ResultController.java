package cn.bytecloud.steam.result.controller;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.annotation.Menu;
import cn.bytecloud.steam.annotation.Permission;
import cn.bytecloud.steam.base.dto.APIResult;
import cn.bytecloud.steam.result.dto.AddResultDto;
import cn.bytecloud.steam.result.dto.ResultPageDto;
import cn.bytecloud.steam.result.dto.UpdResultDto;
import cn.bytecloud.steam.result.service.ResultService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Menu("比赛结果")
public class ResultController {

    public static final String API = "/api/result/";

    @Autowired
    private ResultService service;

    @PostMapping(API + "add")
    @RequiresPermissions({API + "add"})
    @Permission("添加")
    public APIResult add(@RequestBody @Validated AddResultDto dto) {
        return APIResult.success().setValue(service.save(dto));
    }

    @PostMapping(API + "upd")
    @RequiresPermissions({API + "upd"})
    @Permission("修改")
    public APIResult upd(@RequestBody @Validated UpdResultDto dto) {
        return APIResult.success().setValue(service.save(dto));
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
    public APIResult list(@Validated ResultPageDto dto) {
        return APIResult.success().setValue(service.list(dto));
    }

    @GetMapping("/api/home/result")
    public APIResult homelist(@Validated ResultPageDto dto) {
        return APIResult.success().setValue(service.list(dto));
    }
}
