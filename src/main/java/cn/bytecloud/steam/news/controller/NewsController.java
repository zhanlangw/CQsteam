package cn.bytecloud.steam.news.controller;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.annotation.Menu;
import cn.bytecloud.steam.annotation.Permission;
import cn.bytecloud.steam.base.dto.APIResult;
import cn.bytecloud.steam.base.dto.BasePageDto;
import cn.bytecloud.steam.news.dto.AddNewsDto;
import cn.bytecloud.steam.news.dto.NewsPageDto;
import cn.bytecloud.steam.news.dto.UpdNewsDto;
import cn.bytecloud.steam.news.service.NewsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Menu("新闻动态")
public class NewsController {

    public static final String API = "/api/news/";

    @Autowired
    private NewsService service;

    @PostMapping(API + "add")
    @RequiresPermissions({API + "add"})
    @Permission("添加")
    public APIResult add(@Validated @RequestBody AddNewsDto dto) {
        return APIResult.success().setValue(service.save(dto));
    }

    @PostMapping(API + "upd")
    @RequiresPermissions({API + "upd"})
    @Permission("修改")
    public APIResult upd(@Validated @RequestBody UpdNewsDto dto) {
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
    public APIResult list(@Valid NewsPageDto dto) {
        return APIResult.success().setValue(service.list(dto));
    }

    @GetMapping("/api/home/news")
    public APIResult homeList(@Validated BasePageDto dto) {
        return APIResult.success().setValue(service.homeList(dto));
    }

    @GetMapping("/api/home/news/item")
    public APIResult homeItem(@RequestParam String id) {
        return APIResult.success().setValue(service.item(id));
    }
}
