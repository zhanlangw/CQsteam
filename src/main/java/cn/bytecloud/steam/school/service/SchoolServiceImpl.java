package cn.bytecloud.steam.school.service;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.exception.ErrorCode;
import cn.bytecloud.steam.area.dao.AreaRepository;
import cn.bytecloud.steam.area.entity.Area;
import cn.bytecloud.steam.base.dto.PageModel;
import cn.bytecloud.steam.category.entity.GroupType;
import cn.bytecloud.steam.school.dao.SchoolDao;
import cn.bytecloud.steam.school.dao.SchoolRepository;
import cn.bytecloud.steam.school.dto.AddSchoolDto;
import cn.bytecloud.steam.school.dto.SchoolPageDto;
import cn.bytecloud.steam.school.entity.School;
import cn.bytecloud.steam.util.FileUtil;
import cn.bytecloud.steam.util.UUIDUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

@Service
public class SchoolServiceImpl implements SchoolService {
    @Autowired
    private SchoolRepository repository;

    @Autowired
    private SchoolDao dao;

    @Autowired
    private AreaRepository areaRepository;

    @Override
    public void importData(HttpServletRequest request) throws IOException, FileUploadException, ByteException {
        FileItem file = FileUtil.getFile(request);
        Workbook workbook = new HSSFWorkbook((file.getInputStream()));
        Sheet sheet = workbook.getSheetAt(0);

        repository.deleteAll();
        Area byName = areaRepository.findByName("重庆市");
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }

            Cell cell = row.getCell(1);
            cell.setCellType(CellType.STRING);
            String name = cell.getStringCellValue();
            Cell cell1 = row.getCell(2);
            cell1.setCellType(CellType.STRING);
            String areaName = cell1.getStringCellValue();
            String[] split = areaName.split(" ");

            if ("大足区".equals(split[2])) {
                split[2] = "大足县";
            }
            if ("綦江区".equals(split[2])) {
                split[2] = "綦江县";
            }
            if ("荣昌区".equals(split[2])) {
                split[2] = "荣昌县";
            }
            if ("铜梁区".equals(split[2])) {
                split[2] = "铜梁县";
            }
            if ("璧山区".equals(split[2])) {
                split[2] = "璧山县";
            }
            if ("潼南区".equals(split[2])) {
                split[2] = "潼南县";
            }
            if ("梁平区".equals(split[2])) {
                split[2] = "梁平县";
            }
            if ("开州区".equals(split[2])) {
                split[2] = "开县";
            }
            if ("开州县".equals(split[2])) {
                split[2] = "开县";
            }
            if ("武隆区".equals(split[2])) {
                split[2] = "武隆县";
            }

            if ("潼南区".equals(split[2])) {
                split[2] = "潼南县";
            }
            Area area2 = areaRepository.findByNameAndParentId(split[2], byName.getId());
            School school = new School();
            school.setId(UUIDUtil.getUUID());
            school.setAreaId(area2.getId());
            school.setName(name);
            school.setCreateTime(System.currentTimeMillis());

            cell = row.getCell(3);
            cell.setCellType(CellType.STRING);
            String group = cell.getStringCellValue().substring(0, 2);
            school.setGroup(GroupType.getByName(group));
            repository.save(school);
        }
    }

    @Override
    public Object save(AddSchoolDto dto) throws ByteException {
        if (null == areaRepository.findOne(dto.getAreaId()) || areaRepository.findByParentId(dto.getAreaId()).size()
                > 0) {
            throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"areaId"});
        }
        if (repository.findByAreaIdAndNameAndGroup(dto.getAreaId(), dto.getName(),GroupType.getType(dto.getGroup())).size() > 0) {
            throw new ByteException(ErrorCode.EXISTS_PARAMETER, new String[]{"name"});
        }
        return dao.save(dto.toData()).toDto();
    }

    @Override
    public Object list(SchoolPageDto dto) {
        PageModel<School> pageModel = dao.list(dto);
        return new PageModel<>(pageModel.getTotalCount(), pageModel.getValue().stream().map(School::toDto).collect
                (Collectors.toList()));
    }


    @Override
    public School findOne(String schoolId) {
        return repository.findOne(schoolId);
    }
}
