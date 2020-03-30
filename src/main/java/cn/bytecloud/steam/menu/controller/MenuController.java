package cn.bytecloud.steam.menu.controller;

import cn.bytecloud.steam.base.dto.APIResult;
import cn.bytecloud.steam.menu.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private MenuService service;

    @GetMapping("list")
    public APIResult list(){
        return APIResult.success().setValue(service.list());
    }
}
