package cn.bytecloud.steam.export.service;

import cn.bytecloud.steam.base.dto.PageModel;
import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.exception.ErrorCode;
import cn.bytecloud.steam.export.dao.ExportDao;
import cn.bytecloud.steam.export.dao.ExportRepository;
import cn.bytecloud.steam.export.dto.AddExportDto;
import cn.bytecloud.steam.export.dto.ExportPageDto;
import cn.bytecloud.steam.export.entity.Export;
import cn.bytecloud.steam.export.entity.ExportStatus;
import cn.bytecloud.steam.export.thread.ZipHandler;
import cn.bytecloud.steam.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

@Service
public class ExportServiceImpl implements ExportService {
    @Autowired
    private ExportRepository repository;

    @Autowired
    private ExportDao dao;

    @Autowired
    private ZipHandler handler;

    @Override
    public void add(AddExportDto dto) throws ByteException {
        Export export = dto.toData();
        //限制导出数量
        if (repository.findAll().stream().anyMatch(data -> data.getStatus() == ExportStatus.EXPORTING)) {
            throw new ByteException(ErrorCode.FAILURE, "请等待上个导出任务结束!");
        }
        repository.save(export);
        handler.getInstance().addZipMsg(export, dto.getType());
    }

    @Override
    public void save(Export export) {
        repository.save(export);
    }

    @Override
    public void download(HttpServletResponse response, String id, HttpServletRequest request) throws IOException {
        Export export = repository.findOne(id);
        ServletOutputStream out = response.getOutputStream();
        FileInputStream in = new FileInputStream(export.getPath());

        String fileName = "智能导出";
        String userAgent = request.getHeader("user-agent");
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {//IE内核
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } else {//非IE内核
            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
        }
        fileName += ".zip";
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/download");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        byte[] bytes = new byte[1024];
        int num;
        while ((num = in.read(bytes)) != -1) {
            out.write(bytes, 0, num);
        }
        in.close();
        out.close();
    }

    @Override
    public void del(String id) throws ByteException {
        Export export = repository.findOne(id);
        if (export.getStatus() == ExportStatus.EXPORTING) {
            throw new ByteException(ErrorCode.FAILURE, "正在导出的禁止删除");
        }
        if (export.getStatus() == ExportStatus.SUCCESS) {
            File file = new File(export.getPath());
            if (file.exists()) {
                file.delete();
            }
        }
        repository.delete(id);
    }

    @Override
    public PageModel list(ExportPageDto dto) {
        PageModel<Export> pageModel = dao.list(dto);
        List list = new ArrayList();
        pageModel.getValue().forEach(export -> {
            Map map = new HashMap();
            map.put("id", export.getId());
            map.put("name", export.getName());
            map.put("createTime", StringUtil.getTime(new Date(export.getCreateTime())));
            map.put("status", export.getStatus().getEnumValue());
            map.put("downloadUrl", export.getDownloadUrl() == null ? "" : export.getDownloadUrl());
            list.add(map);
        });
        return new PageModel<>(pageModel.getTotalCount(), list);
    }
}
