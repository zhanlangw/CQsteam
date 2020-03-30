package cn.bytecloud.steam.export.dao;

import cn.bytecloud.steam.base.dto.PageModel;
import cn.bytecloud.steam.export.dto.ExportPageDto;
import cn.bytecloud.steam.export.entity.Export;

public interface ExportDao {
    PageModel<Export> list(ExportPageDto dto);
}
