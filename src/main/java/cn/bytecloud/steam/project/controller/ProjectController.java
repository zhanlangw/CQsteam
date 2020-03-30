package cn.bytecloud.steam.project.controller;

import cn.bytecloud.steam.annotation.Menu;
import cn.bytecloud.steam.annotation.Permission;
import cn.bytecloud.steam.base.dto.APIResult;
import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.project.dto.*;
import cn.bytecloud.steam.project.service.ProjectService;
import cn.bytecloud.steam.util.FileUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Menu("大赛管理")
public class ProjectController {
    public static final String API_SIGN_UP = "/api/project/sign_up/";
    public static final String API = "/api/project/";

    @Autowired
    private ProjectService service;

    @PostMapping(API_SIGN_UP + "basis/add")
    public APIResult addBasis(@RequestBody @Validated AddBasisDto dto) throws ByteException {
        return APIResult.success().setValue(service.addBasis(dto));
    }

    @PostMapping(API_SIGN_UP + "basis/upd")
    public APIResult updBasis(@RequestBody @Validated UpdBasisDto dto) throws ByteException {
        return APIResult.success().setValue(service.updBasis(dto));
    }

    @GetMapping(API_SIGN_UP + "basis/item")
    public APIResult itemBasis(@RequestParam String id) throws ByteException {
        return APIResult.success().setValue(service.itemBasis(id));
    }

    @PostMapping(API_SIGN_UP + "basis/project/add")
    public APIResult addBasisProject(@RequestBody @Validated AddBasisProjectDto dto) throws ByteException {
        return APIResult.success().setValue(service.addBasisProject(dto));
    }

    @GetMapping(API_SIGN_UP + "basis/project/item")
    public APIResult itemBasisProject(@RequestParam String id) throws ByteException {
        return APIResult.success().setValue(service.itemBasisProject(id));
    }

    @PostMapping(API_SIGN_UP + "/teacher/add")
    public APIResult addTeacher(@RequestBody @Validated AddTeacherDto dto) throws ByteException {
        return APIResult.success().setValue(service.addTeacher(dto));
    }

    @GetMapping(API_SIGN_UP + "teacher/item")
    public APIResult itemTeacher(@RequestParam String id) throws ByteException {
        return APIResult.success().setValue(service.itemTeacher(id));
    }

    @PostMapping(API_SIGN_UP + "/material/add")
    public APIResult addMaterial(@RequestBody @Validated AddMaterialDto dto) throws ByteException {
        return APIResult.success().setValue(service.addMaterial(dto));
    }

    @GetMapping(API_SIGN_UP + "/material/item")
    public APIResult itemMaterial(@RequestParam String id, @RequestParam Integer stageType) throws ByteException {
        return APIResult.success().setValue(service.itemMaterial(id, stageType));
    }

    @GetMapping(API_SIGN_UP + "/status")
    public APIResult status(@RequestParam String id) throws ByteException {
        return APIResult.success().setValue(service.status(id));
    }

    @GetMapping(API_SIGN_UP + "/submit")
    public APIResult submit(@RequestParam String id) throws ByteException {
        service.submit(id);
        return APIResult.success();
    }

    @PostMapping(API_SIGN_UP + "upload")
    public APIResult importFile(@RequestParam String id, @RequestParam Integer stage, @RequestParam Integer fileType,
                                HttpServletRequest request) throws FileUploadException, IOException, ByteException {
        return APIResult.success().setValue(service.importFile(id, stage, fileType, request));
    }

    @GetMapping(API_SIGN_UP + "file/del")
    public APIResult delFile(@RequestParam String path) throws FileUploadException, IOException, ByteException {
//        service.delFile(path);
        return APIResult.success();
    }

    @GetMapping(API_SIGN_UP + "del/file")
    public APIResult delFile(@RequestParam String path, @RequestParam Integer stage, @RequestParam String id,
                             @RequestParam Integer fileType) throws FileUploadException, IOException, ByteException {
        service.delFile(path, stage, id, fileType);
        return APIResult.success();
    }

    @GetMapping(API + "list")
    @RequiresPermissions({API + "list"})
    @Permission("列表")
    public APIResult list(@Validated ProjectPageDto dto) {
        return APIResult.success().setValue(service.list(dto));
    }

    @GetMapping(API + "export")
    @RequiresPermissions({API + "export"})
    @Permission("导出excel")
    public void export(ProjectPageDto dto, HttpServletResponse response,HttpServletRequest request) throws IOException {
        service.export(dto, response,request);
    }

    @GetMapping(API + "send_msg")
    @RequiresPermissions({API + "send_msg"})
    @Permission("短信提醒")
    public APIResult sendMsg(ProjectPageDto dto) {
        service.sendMsg(dto);
        return APIResult.success();
    }

    @PostMapping(API + "import_result")
    @RequiresPermissions({API + "import_result"})
    @Permission("结果批量导入")
    public APIResult importResult(HttpServletRequest request, @RequestParam String categoryId, @RequestParam Integer
            stage) throws FileUploadException, IOException, ByteException {
        return APIResult.success().setValue(service.importResult(FileUtil.getFile(request), categoryId, stage));
    }

    @GetMapping(API + "del")
    public APIResult del(@RequestParam String id) throws IOException, ByteException {
        service.del(id);
        return APIResult.success();
    }

    @GetMapping(API + "admin/del")
    @RequiresPermissions({API + "admin/del"})
    @Permission("删除")
    public APIResult adminDel(@RequestParam String id) throws IOException {
        service.adminDel(id);
        return APIResult.success();
    }
}
