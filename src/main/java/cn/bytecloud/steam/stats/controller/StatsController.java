package cn.bytecloud.steam.stats.controller;

import cn.bytecloud.steam.annotation.Menu;
import cn.bytecloud.steam.annotation.Permission;
import cn.bytecloud.steam.base.dto.APIResult;
import cn.bytecloud.steam.stats.dto.AddStatsDto;
import cn.bytecloud.steam.stats.dto.ExportDto1;
import cn.bytecloud.steam.stats.dto.ExportDto2;
import cn.bytecloud.steam.stats.dto.StatsDto;
import cn.bytecloud.steam.stats.service.StatsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Menu("数据统计")
public class StatsController {
    public static final String API = "/api/stats/";

    @Autowired
    private StatsService service;

    @GetMapping(API + "sign_up")
    @RequiresPermissions({API + "sign_up"})
    @Permission("报名数据")
    public APIResult signUpStats(@Validated StatsDto dTo) {
        return APIResult.success().setValue(service.signUpStats(dTo));
    }

    @GetMapping(API + "sign_up/export")
    @RequiresPermissions({API + "sign_up/export"})
    @Permission("导出报名数据")
    public void exportSignUp(@Validated ExportDto1 dto1, @Validated ExportDto2 dto2, HttpServletResponse response,HttpServletRequest request)
            throws IOException {
        service.exportSignUp(dto1, dto2, response,request);
    }

    @GetMapping(API + "register")
    @RequiresPermissions({API + "register"})
    @Permission("注册数据")
    public APIResult registerStats(@Validated StatsDto dto) {
        return APIResult.success().setValue(service.registerStats(dto));
    }

    @GetMapping(API + "register/export")
    @RequiresPermissions({API + "register"})
    @Permission("导出注册数据")
    public void exportRegister(@Validated ExportDto1 dto1, @Validated ExportDto2 dto2, HttpServletResponse response,HttpServletRequest request)
            throws IOException {
        service.exportRegister(dto1, dto2, response,request);
    }

    @GetMapping(API + "word")
    @RequiresPermissions({API + "word"})
    @Permission("提交word数据")
    public APIResult wordStats(@Validated StatsDto dto) {
        return APIResult.success().setValue(service.wordStats(dto));
    }


    @GetMapping(API + "word/export")
    @RequiresPermissions({API + "word/export"})
    @Permission("导出提交word数据")
    public void exportWord(@Validated ExportDto1 dto1, @Validated ExportDto2 dto2, HttpServletResponse response,HttpServletRequest request)
            throws IOException {
        service.exportWord(dto1, dto2, response,request);
    }

    @GetMapping(API + "submit")
    @RequiresPermissions({API + "submit"})
    @Permission("最终确认文档数据")
    public APIResult submitStats(@Validated StatsDto dto) {
        return APIResult.success().setValue(service.submitStats(dto));
    }

    @GetMapping(API + "submit/export")
    @RequiresPermissions({API + "submit/export"})
    @Permission("导出最终确认文档数据")
    public void exportSubmit(@Validated ExportDto1 dto1, @Validated ExportDto2 dto2,HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        service.exportSubmit(dto1, dto2, response,request);
    }

    @GetMapping(API + "prize")
    @RequiresPermissions({API + "prize"})
    @Permission("获奖等级统计")
    public APIResult prizeStats(StatsDto dto) {
        return APIResult.success().setValue(service.prizeStats(dto));
    }

    @GetMapping(API + "prize/export")
    @RequiresPermissions({API + "prize/export"})
    @Permission("导出获奖等级数据")
    public void exportPrize(ExportDto1 dto1, ExportDto2 dto2, HttpServletResponse response, HttpServletRequest request)
            throws IOException {
        service.exportPrize(dto1, dto2, response,request);
    }

    @PostMapping(API + "add")
    public APIResult add(@Validated @RequestBody AddStatsDto dto) {
        service.add(dto);
        return APIResult.success();
    }

    @GetMapping(API + "visitor")
    @RequiresPermissions({API + "visitor"})
    @Permission("访客统计")
    public APIResult visitor(@Validated StatsDto dto) {
        return APIResult.success().setValue(service.visitor(dto));
    }

    @GetMapping(API + "visitor/export")
    @RequiresPermissions({API + "visitor/export"})
    @Permission("导出访客统计数据")
    public void exportVisitor(@Validated StatsDto dto, HttpServletResponse response,HttpServletRequest request)
            throws IOException {
        service.exportVisitor(dto, response,request);
    }
}
