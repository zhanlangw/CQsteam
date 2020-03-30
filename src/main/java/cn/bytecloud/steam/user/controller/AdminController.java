package cn.bytecloud.steam.user.controller;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.annotation.Menu;
import cn.bytecloud.steam.annotation.Permission;
import cn.bytecloud.steam.base.dto.APIResult;
import cn.bytecloud.steam.user.dto.*;
import cn.bytecloud.steam.user.service.UserService;
import cn.bytecloud.steam.util.MD5Util;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Menu("管理员管理")
public class AdminController {

    public static final String API = "/api/admin/";

    @Autowired
    private UserService service;

    @PostMapping(API + "add")
    @RequiresPermissions({API + "add"})
    @Permission("添加")
    public APIResult add(@RequestBody @Validated AddUserDto dto) throws ByteException {
        return APIResult.success().setValue(service.save(dto));
    }

    @PostMapping(API + "upd")
    @RequiresPermissions({API + "upd"})
    @Permission("修改")
    public APIResult upd(@RequestBody @Validated UpdUserDto dto) throws ByteException {
        return APIResult.success().setValue(service.upd(dto));
    }

    @PostMapping(API + "password/reset")
    @RequiresPermissions({API + "password/reset"})
    @Permission("重置密码")
    public APIResult resetPasswrod(@RequestBody @Validated ResetPasswordDto dto) throws ByteException {
        service.resetPasswrod(dto);
        return APIResult.success();
    }


    @GetMapping(API + "del")
    @RequiresPermissions({API + "del"})
    @Permission("删除")
    public APIResult del(@RequestParam String id) throws ByteException {
        service.del(id);
        return APIResult.success();
    }

    @GetMapping(API + "item")
    @RequiresPermissions({API + "item"})
    @Permission("详情")
    public APIResult item(@RequestParam String id) throws ByteException {
        return APIResult.success().setValue(service.item(id));
    }


    @GetMapping(API + "list")
    @RequiresPermissions({API + "list"})
    @Permission("列表")
    public APIResult list(@Validated PageUserDto dto) throws ByteException {
        return APIResult.success().setValue(service.list(dto, false));
    }

    @PostMapping(API + "login")
    public APIResult login(@RequestBody @Validated LoginDto dto, HttpServletRequest request) throws ByteException {
        Subject subject = SecurityUtils.getSubject();
//        if (subject.isAuthenticated()) {
//            return APIResult.failure(ErrorCode.FAILURE).setMessage("已经登录");
//        }
        subject.login(new UsernamePasswordToken(dto.getUsername()+"_admin", MD5Util.getMD5(dto.getPassword())));
        return APIResult.success().setValue(dto.getUsername());
    }

}
