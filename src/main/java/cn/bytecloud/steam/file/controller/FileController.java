package cn.bytecloud.steam.file.controller;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.base.dto.APIResult;
import cn.bytecloud.steam.file.service.FileService;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileService service;

    @PostMapping("/upload")
    public APIResult upload(HttpServletRequest request, @RequestParam Integer fileType) throws FileUploadException,
            IOException, ByteException {
        return APIResult.success().setValue(service.upload(request, fileType));
    }
}
