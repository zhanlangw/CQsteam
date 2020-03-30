package cn.bytecloud.steam.result.dao;

import cn.bytecloud.steam.base.dto.PageModel;
import cn.bytecloud.steam.result.dto.ResultPageDto;
import cn.bytecloud.steam.result.entity.Result;

import java.util.HashMap;

public interface ResultDao {
    Result save(Result result);

    PageModel<HashMap> list(ResultPageDto dto);
}
