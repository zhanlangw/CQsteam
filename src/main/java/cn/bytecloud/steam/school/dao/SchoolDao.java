package cn.bytecloud.steam.school.dao;

import cn.bytecloud.steam.base.dto.PageModel;
import cn.bytecloud.steam.school.dto.SchoolPageDto;
import cn.bytecloud.steam.school.entity.School;

public interface SchoolDao {
    School save(School school);

    PageModel<School> list(SchoolPageDto dto);
}
