package cn.bytecloud.steam.school.service;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.school.dto.AddSchoolDto;
import cn.bytecloud.steam.school.dto.SchoolPageDto;
import cn.bytecloud.steam.school.entity.School;
import org.apache.commons.fileupload.FileUploadException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface SchoolService {
    void importData(HttpServletRequest inputStream) throws IOException, FileUploadException, ByteException;

    Object save(AddSchoolDto dto) throws ByteException;

    Object list(SchoolPageDto dto);

    School findOne(String schoolId);
}
