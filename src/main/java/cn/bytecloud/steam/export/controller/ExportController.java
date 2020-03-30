package cn.bytecloud.steam.export.controller;

import cn.bytecloud.steam.annotation.Menu;
import cn.bytecloud.steam.annotation.Permission;
import cn.bytecloud.steam.base.dto.APIResult;
import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.export.dto.AddExportDto;
import cn.bytecloud.steam.export.dto.ExportPageDto;
import cn.bytecloud.steam.export.service.ExportService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Menu("智能导出")
public class ExportController {
    public static final String API = "/api/export/";

    @Autowired
    private ExportService service;

    @PostMapping(API + "add")
    @RequiresPermissions({API + "add"})
    @Permission("添加")
    public APIResult add(@Validated @RequestBody AddExportDto dto) throws ByteException {
        service.add(dto);
        return APIResult.success();
    }

    @GetMapping(API + "download")
//    @RequiresPermissions({API + "download"})
//    @Permission("下载智能导出")
    public void download(@RequestParam String id, HttpServletResponse response, HttpServletRequest request) throws IOException {
        service.download(response, id,request);
    }

    @GetMapping(API + "del")
    @RequiresPermissions({API + "del"})
    @Permission("删除智能导出")
    public APIResult download(@RequestParam String id) throws IOException, ByteException {
        service.del(id);
        return APIResult.success();
    }

    @GetMapping(API + "list")
    @RequiresPermissions({API + "list"})
    @Permission("智能导出列表")
    public APIResult list(@Validated ExportPageDto dto) throws IOException {

        return APIResult.success().setValue(service.list(dto));
    }
}
