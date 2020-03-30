package cn.bytecloud.steam.school.controller;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.base.dto.APIResult;
import cn.bytecloud.steam.school.dto.AddSchoolDto;
import cn.bytecloud.steam.school.dto.SchoolPageDto;
import cn.bytecloud.steam.school.service.SchoolService;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class SchoolController {
    public static final String API = "/api/school/";

    @Autowired
    private SchoolService service;

    @PostMapping(API + "import_data")
    public APIResult importData(HttpServletRequest request) throws IOException, FileUploadException, ByteException {
        service.importData(request);
        return APIResult.success();
    }

    @PostMapping(API + "add")
    public APIResult add(@RequestBody @Validated AddSchoolDto dto) throws ByteException {
        return APIResult.success().setValue(service.save(dto));
    }

    @RequestMapping(API + "list")
    public APIResult list(@Validated SchoolPageDto dto) {
        return APIResult.success().setValue(service.list(dto));
    }
}
