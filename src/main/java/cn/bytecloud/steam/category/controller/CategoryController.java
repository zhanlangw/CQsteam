package cn.bytecloud.steam.category.controller;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.exception.ErrorCode;
import cn.bytecloud.steam.annotation.Menu;
import cn.bytecloud.steam.annotation.Permission;
import cn.bytecloud.steam.base.dto.APIResult;
import cn.bytecloud.steam.category.dto.AddCategoryDto;
import cn.bytecloud.steam.category.dto.CategoryPageDto;
import cn.bytecloud.steam.category.dto.UpdCategoryDto;
import cn.bytecloud.steam.category.service.CategoryService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Validated
@Menu("类别")
public class CategoryController {

    public static final String API = "/api/category/";

    @Autowired
    private CategoryService service;

    @RequestMapping(value = "/not_login")
    public APIResult notLogin() {
        return APIResult.failure(ErrorCode.PARAMETER_NOT_LOGIN).setMessage("未登录");
    }

    /**
     * 添加类别
     * @param
     * @return
     */
    @PostMapping(API + "add")
    @RequiresPermissions({API + "add"})
    @Permission("添加")
    public APIResult add(@RequestBody @Validated AddCategoryDto dto) throws ByteException {
        return APIResult.success().setValue(service.save(dto));
    }

    /**
     * 添加类别
     *
     * @param dto
     * @return
     */
    @PostMapping(API + "upd")
    @RequiresPermissions({API + "upd"})
    @Permission("修改")
    public APIResult upd(@RequestBody @Validated UpdCategoryDto dto) throws ByteException {
        return APIResult.success().setValue(service.upd(dto));
    }
//
//    @PostMapping(API + "upd")
//    @RequiresPermissions({API + "upd"})
//    public APIResult updStageTime(@RequestBody @Validated UpdCategoryDto dto) throws ByteException {
//        return APIResult.success().setValue(service.upd(dto));
//    }

    /**
     * 详情
     */
    @GetMapping(API + "item")
    @RequiresPermissions({API + "item"})
    @Permission("详情")
    public APIResult item(@RequestParam String id) throws ByteException {
        return APIResult.success().setValue(service.item(id));
    }

    @GetMapping("/api/home/category/item")
    public APIResult itemHome(@RequestParam String id) throws ByteException {
        return APIResult.success().setValue(service.item(id));
    }

    /**
     * 列表
     *
     * @param pageDto
     * @return
     * @throws ByteException
     */
    @GetMapping(API + "list")
    @RequiresPermissions({API + "list"})
    @Permission("列表")
    public APIResult list(@Valid CategoryPageDto pageDto) throws ByteException {
        return APIResult.success().setValue(service.list(pageDto, false));
    }

    @GetMapping("/api/home/category/list")
    public APIResult listHome(@Valid CategoryPageDto pageDto) throws ByteException {
        return APIResult.success().setValue(service.list(pageDto, true));
    }


    /**
     * 删除
     */
    @GetMapping(API + "del")
    @RequiresPermissions({API + "del"})
    @Permission("删除")
    public APIResult del(@RequestParam String id) throws ByteException {
        service.del(id);
        return APIResult.success();
    }

    @GetMapping(API + "stage")
    public APIResult stage(@RequestParam String id) throws ByteException {
        return APIResult.success().setValue(service.stage(id));
    }
}
