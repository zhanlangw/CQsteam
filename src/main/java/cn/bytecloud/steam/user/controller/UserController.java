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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Menu("用户管理")
public class UserController {
    public static final String API = "/api/user/";

    @Autowired
    private UserService service;

    @GetMapping("/api/project/user/item")
    public APIResult item() throws ByteException {
        return APIResult.success().setValue(service.item());
    }

    @GetMapping(API + "captcha")
    public APIResult captcha(@RequestParam String telephone, @RequestParam Integer type, HttpServletRequest request) throws ByteException {
        service.captcha(telephone, request, type);
        return APIResult.success();
    }

    @PostMapping(API + "register")
    public APIResult register(@RequestBody @Validated UserDto dto, HttpServletRequest request) throws ByteException {
        service.register(dto, request);
        return APIResult.success();
    }

    @PostMapping(API + "password/retrieve")
    public APIResult retrievePassword(@RequestBody @Validated UserDto dto, HttpServletRequest request) throws ByteException {
        service.retrievePassword(dto, request);
        return APIResult.success();
    }

    @PostMapping(API + "password/upd")
    public APIResult updPassword(@RequestBody @Validated UpdPasswordDto dto, HttpServletRequest request) throws ByteException {
        service.updPassword(dto, request);
        return APIResult.success();
    }

    @PostMapping(API + "login")
    public APIResult login(@RequestBody @Validated LoginDto dto, HttpServletRequest request) throws ByteException {
        Subject subject = SecurityUtils.getSubject();
//        if (subject.isAuthenticated()) {
//            return APIResult.failure(ErrorCode.FAILURE).setMessage("已经登录");
//        }
        subject.login(new UsernamePasswordToken(dto.getUsername(), MD5Util.getMD5(dto.getPassword())));
        return APIResult.success().setValue(dto.getUsername());
    }

    @GetMapping(API+"project")
    public APIResult myProject() throws ByteException {
        return APIResult.success().setValue(service.myProject());
    }

    @GetMapping(API + "list")
    @RequiresPermissions({API + "list"})
    @Permission("列表")
    public APIResult list(@Validated PageUserDto dto) throws ByteException {
        return APIResult.success().setValue(service.list(dto, true));
    }


    @PostMapping(API + "password/reset")
    @RequiresPermissions({API + "password/reset"})
    @Permission("重置密码")
    public APIResult resetPasswrod(@RequestBody @Validated ResetPasswordDto dto) throws ByteException {
        service.resetPasswrod(dto);
        return APIResult.success();
    }

    @GetMapping(API + "export")
    @RequiresPermissions({API + "export"})
    @Permission("导出")
    public void export(HttpServletResponse response,HttpServletRequest request) throws ByteException, IOException {
        service.export(response,request);
    }


    @GetMapping(API + "disable")
    @RequiresPermissions({API + "disable"})
    @Permission("禁用")
    public APIResult disable(@RequestParam String id,@RequestParam Boolean flag) throws ByteException, IOException {
        service.disable(id,flag);
        return APIResult.success();
    }

}
