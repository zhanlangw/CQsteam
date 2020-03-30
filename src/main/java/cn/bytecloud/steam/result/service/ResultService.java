package cn.bytecloud.steam.result.service;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.result.dto.AddResultDto;
import cn.bytecloud.steam.result.dto.ResultPageDto;

public interface ResultService {
    Object save(AddResultDto dto);

    void del(String id) throws ByteException;

    Object list(ResultPageDto dto);

    Object item(String id);
}
