package cn.bytecloud.steam.export.service;

import cn.bytecloud.steam.base.dto.PageModel;
import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.export.dto.AddExportDto;
import cn.bytecloud.steam.export.dto.ExportPageDto;
import cn.bytecloud.steam.export.entity.Export;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ExportService {
    void add(AddExportDto type) throws ByteException;

    void save(Export export);

    void download(HttpServletResponse response, String id, HttpServletRequest request) throws IOException;

    void del(String id) throws ByteException;

    PageModel list(ExportPageDto dto);
}
