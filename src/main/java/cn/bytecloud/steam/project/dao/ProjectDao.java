package cn.bytecloud.steam.project.dao;

import cn.bytecloud.steam.base.dto.PageModel;
import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.project.dto.ProjectPageDto;
import cn.bytecloud.steam.project.entity.Project;
import cn.bytecloud.steam.stats.dto.StatsDto;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface ProjectDao {
    List<Project> findByCategoryId(String categoryId);

    Project save(Project project) throws ByteException;

    void submit(String id);

    List<HashMap> myProject();

    PageModel<HashMap> list(ProjectPageDto dto);

    List<Project> findByDate(Date date, String field);

    List<HashMap> signUpStats(StatsDto dto);

    List<HashMap> wordStats(StatsDto dto);

    List<HashMap> submitStats(StatsDto dto);

    List<HashMap> prizeStats(StatsDto dto);

    void eliminate(String id);

    List<HashMap> getExportData(ProjectPageDto dto);

    List<HashMap> getExportStudent();

    List<HashMap> getExportSchool();

}
