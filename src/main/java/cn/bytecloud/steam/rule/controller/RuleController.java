package cn.bytecloud.steam.rule.controller;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.exception.ErrorCode;
import cn.bytecloud.steam.annotation.Menu;
import cn.bytecloud.steam.annotation.Permission;
import cn.bytecloud.steam.base.dto.APIResult;
import cn.bytecloud.steam.rule.dto.RuleDto;
import cn.bytecloud.steam.rule.dto.RulePageDto;
import cn.bytecloud.steam.rule.service.RuleService;
import cn.bytecloud.steam.user.entity.User;
import cn.bytecloud.steam.util.EmptyUtil;
import cn.bytecloud.steam.util.UserUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Menu("规则")
public class RuleController {

    public static final String API = "/api/rule/";

    @Autowired
    private RuleService service;

    @PostMapping(API + "add")
    @RequiresPermissions(API + "add")
    @Permission("添加")
    public APIResult add(@Validated @RequestBody RuleDto dto) {
        return APIResult.success().setValue(service.save(dto));
    }

    @PostMapping(API + "upd")
    @RequiresPermissions(API + "upd")
    @Permission("修改")
    public APIResult upd(@Validated @RequestBody RuleDto dto) throws ByteException {
        if (EmptyUtil.isEmpty(dto.getId())) {
            throw new ByteException(ErrorCode.NULL_PARAMETER, new String[]{"id"});
        }
        return APIResult.success().setValue(service.save(dto));
    }

    @GetMapping(API + "item")
    @RequiresPermissions(API + "item")
    @Permission("详情")
    public APIResult item(@RequestParam String id) {
        return APIResult.success().setValue(service.item(id));
    }

    @GetMapping(API + "del")
    @RequiresPermissions(API + "del")
    @Permission("删除")
    public APIResult del(@RequestParam String id) throws ByteException {
        service.del(id);
        return APIResult.success();
    }

    @GetMapping(API + "list")
    @RequiresPermissions(API + "list")
    @Permission("列表")
    public APIResult list(@Validated RulePageDto dto) {
        User user = UserUtil.getUser();
        return APIResult.success().setValue(service.list(dto));
    }

    @GetMapping("/api/home/rule")
    public APIResult homeList() {
        return APIResult.success().setValue(service.homeList());
    }

    @GetMapping("/api/home/rule/item")
    public APIResult homeItem(@RequestParam String id) {
        return APIResult.success().setValue(service.homeItem(id));
    }
}
