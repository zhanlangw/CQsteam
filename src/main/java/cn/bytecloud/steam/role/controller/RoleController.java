package cn.bytecloud.steam.role.controller;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.annotation.Menu;
import cn.bytecloud.steam.annotation.Permission;
import cn.bytecloud.steam.base.dto.APIResult;
import cn.bytecloud.steam.role.dto.AddRoleDto;
import cn.bytecloud.steam.role.dto.PageRoleDto;
import cn.bytecloud.steam.role.dto.UpdRoleDto;
import cn.bytecloud.steam.role.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Menu("角色")
public class RoleController {

    public static final String API = "/api/role/";

    @Autowired
    private RoleService service;

    @PostMapping(API + "add")
    @RequiresPermissions({API + "add"})
    @Permission("添加")
    public APIResult add(@RequestBody @Validated AddRoleDto dto) throws ByteException {
        return APIResult.success().setValue(service.save(dto));
    }

    @GetMapping(API + "item")
    @RequiresPermissions({API + "item"})
    @Permission("详情")
    public APIResult item(@RequestParam String id) throws ByteException {
        return APIResult.success().setValue(service.item(id));
    }

    @PostMapping(API + "upd")
    @RequiresPermissions({API + "upd"})
    @Permission("修改")
    public APIResult upd(@RequestBody @Validated UpdRoleDto dto) throws ByteException {
        return APIResult.success().setValue(service.upd(dto));
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
    public APIResult list(@Validated PageRoleDto dto) throws ByteException {
        return APIResult.success().setValue(service.list(dto));
    }
}
