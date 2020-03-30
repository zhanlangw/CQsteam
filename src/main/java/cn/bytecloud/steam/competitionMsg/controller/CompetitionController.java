package cn.bytecloud.steam.competitionMsg.controller;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.annotation.Menu;
import cn.bytecloud.steam.annotation.Permission;
import cn.bytecloud.steam.base.dto.APIResult;
import cn.bytecloud.steam.competitionMsg.dto.CompetitionDto;
import cn.bytecloud.steam.competitionMsg.entity.CompetitionType;
import cn.bytecloud.steam.competitionMsg.service.CompetitionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Menu("大赛简介，须知")
public class CompetitionController {
    public static final String API = "/api/competition";

    @Autowired
    private CompetitionService service;

    @GetMapping(API + "/introduction/item")
    @RequiresPermissions(API + "/introduction/item")
    @Permission("大赛简介详情")
    public APIResult introductionItem() {
        return APIResult.success().setValue(service.itme(CompetitionType.INTRODUCTION));
    }

    @GetMapping(API + "/notice/item")
    @RequiresPermissions(API + "/notice/item")
    @Permission("大赛须知详情")
    public APIResult noticeItem() {
        return APIResult.success().setValue(service.itme(CompetitionType.NOTICE));
    }

    @PostMapping(API + "/introduction/upd")
    @RequiresPermissions(API + "/introduction/upd")
    @Permission("修改大赛简介")
    public APIResult introductionUpd(@Validated @RequestBody CompetitionDto dto) throws ByteException {
        return APIResult.success().setValue(service.upd(dto, CompetitionType.INTRODUCTION));
    }

    @PostMapping(API + "/notice/upd")
    @RequiresPermissions(API + "/notice/upd")
    @Permission("修改大赛须知")
    public APIResult noticeUpd(@Validated @RequestBody CompetitionDto dto) throws ByteException {
        return APIResult.success().setValue(service.upd(dto, CompetitionType.NOTICE));
    }

    @GetMapping("/api/home/notice")
    public APIResult homeNotice() {
        return APIResult.success().setValue(service.homeNotice(CompetitionType.NOTICE));
    }

    @GetMapping("/api/home/introduction")
    public APIResult homeIntroduction() {
        return APIResult.success().setValue(service.homeNotice(CompetitionType.INTRODUCTION));
    }
}
