package cn.bytecloud.steam.project.service;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.project.dto.*;
import cn.bytecloud.steam.project.entity.Project;
import cn.bytecloud.steam.stats.dto.StatsDto;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ProjectService {
    List<Project> findByCategoryId(String id);

    Object addBasis(AddBasisDto dto) throws ByteException;

    Object itemBasis(String id);

    Object addBasisProject(AddBasisProjectDto dto) throws ByteException;

    Object itemBasisProject(String id);

    Object addTeacher(AddTeacherDto list) throws ByteException;

    Object itemTeacher(String id);

    Object addMaterial(AddMaterialDto dto) throws ByteException;

    Object itemMaterial(String id, Integer stageType);

    Object status(String id);

    void submit(String id) throws ByteException;

    Object updBasis(UpdBasisDto dto) throws ByteException;

    Object importFile(String id, Integer stage, Integer type, HttpServletRequest request) throws FileUploadException,
            ByteException, IOException;


    void delFile(String path) throws ByteException;

    List<HashMap> myProject() throws ByteException;

    Object list(ProjectPageDto dto);

    void export(ProjectPageDto dto, HttpServletResponse request, HttpServletRequest httpServletRequest) throws IOException;

    void sendMsg(ProjectPageDto dto);

    Object importResult(FileItem file, String categoryId, Integer stage) throws IOException, ByteException;

    List<Project> findAll();

    List<Project> findByDate(Date date, String createTime);

    List<HashMap> signUpStats(StatsDto dto);

    List<HashMap> wordStats(StatsDto dto);

    Long wordCount();

    List<HashMap> submitStats(StatsDto dto);

    Integer submitCount();

    Map[] prizeStats(StatsDto dto);

    void del(String id) throws ByteException;

    List<Project> findByDocsubmitFlag(boolean flag);

    void delFile(String path, Integer stage, String id,Integer fileType) throws ByteException;

    void adminDel(String id);

    void exportStudent(HttpServletResponse response, HttpServletRequest request) throws IOException;

    void exportSchool(HttpServletResponse response, HttpServletRequest request) throws IOException;

    void exportArea(HttpServletResponse response, HttpServletRequest request) throws IOException;
}
