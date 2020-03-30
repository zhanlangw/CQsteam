package cn.bytecloud.steam.controller;

import cn.bytecloud.steam.area.dao.AreaDao;
import cn.bytecloud.steam.area.dao.AreaRepository;
import cn.bytecloud.steam.base.dto.APIResult;
import cn.bytecloud.steam.category.dao.CategoryDao;
import cn.bytecloud.steam.category.dao.CategoryRepository;
import cn.bytecloud.steam.category.entity.GroupType;
import cn.bytecloud.steam.category.entity.StageType;
import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.exception.ErrorCode;
import cn.bytecloud.steam.file.service.FileService;
import cn.bytecloud.steam.project.dao.ProjectRepository;
import cn.bytecloud.steam.project.entity.*;
import cn.bytecloud.steam.project.service.ProjectService;
import cn.bytecloud.steam.school.dao.SchoolDao;
import cn.bytecloud.steam.school.dao.SchoolRepository;
import cn.bytecloud.steam.school.entity.School;
import cn.bytecloud.steam.user.entity.UserType;
import cn.bytecloud.steam.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static cn.bytecloud.steam.project.entity.SubmitType.*;

@RestController
@Slf4j
//@RequestMapping("api")
public class Controller {

    FileService fileService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectRepository repository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private AreaDao areaDao;
    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private SchoolDao schoolDao;

    @GetMapping("student")
    public void exportStudent(HttpServletResponse response, HttpServletRequest request) throws IOException,
            ByteException {
        checkRoot();
        projectService.exportStudent(response, request);
    }

    @GetMapping("school")
    public void exportSchool(HttpServletResponse response, HttpServletRequest request) throws IOException,
            ByteException {
        checkRoot();
        projectService.exportSchool(response, request);
    }

    @GetMapping("area")
    public void exportArea(HttpServletResponse response, HttpServletRequest request) throws IOException, ByteException {
        projectService.exportArea(response, request);
    }

    public void checkRoot() throws ByteException {
        if (UserUtil.getUser().getType() != UserType.ROOT) {
            throw new ByteException(ErrorCode.AUTHORIZATION);
        }
    }
    @GetMapping("addpath")
    public APIResult path(){
        repository.findAll().forEach(project -> {
            AtomicBoolean flag = new AtomicBoolean(false);
            List<Material> materials = project.getMaterials();
            List<Material> list = new ArrayList<>();
            materials.forEach(material -> {
                Material data = new Material();
                data.setStageType(material.getStageType());
                List<String> paths = new ArrayList<>();
                for (String path : material.getImagePath()) {
                    if (!path.startsWith("/uploads")) {
                        path = "/uploads" + path;
                        flag.set(true);
                    }
                    paths.add(path);
                }
                data.setImagePath(paths);
                paths = new ArrayList<>();
                for (String path : material.getDocPath()) {
                    if (!path.startsWith("/uploads")) {
                        path = "/uploads" + path;
                        flag.set(true);
                    }
                    paths.add(path);
                }
                data.setDocPath(paths);
                paths = new ArrayList<>();
                for (String path : material.getVideoPath()) {
                    if (!path.startsWith("/uploads")) {
                        path = "/uploads" + path;
                        flag.set(true);
                    }
                    paths.add(path);
                }
                data.setVideoPath(paths);
                paths = new ArrayList<>();
                for (String path : material.getPptPath()) {
                    if (!path.startsWith("/uploads")) {
                        path = "/uploads" + path;
                        flag.set(true);
                    }
                    paths.add(path);
                }
                data.setPptPath(paths);
                list.add(data);
            });
            project.setMaterials(list);
            if (flag.get()) {
                repository.save(project);
            }
        });

        return APIResult.success();
    }

    @PostMapping("savepath")
    public APIResult savepath(@RequestBody MultipartFile file) throws IOException, ByteException {
        Map<String, Project> map = new HashedMap<>();

        for (Row row : new XSSFWorkbook(file.getInputStream()).getSheetAt(0)) {
            if (row.getRowNum() == 0) {
                // 第一行 跳过
                continue;
            }


            //编号
            Cell cell0 = row.getCell(0);
            if (cell0 == null) {
                continue;
            }
            cell0.setCellType(CellType.STRING);
            String num = cell0.getStringCellValue();

            Cell cell1 = row.getCell(1);
            if (cell1 == null) {
                continue;
            }
            cell0.setCellType(CellType.STRING);
            String path = cell1.getStringCellValue();


            if (EmptyUtil.isNotEmpty(num) && EmptyUtil.isNotEmpty(path)) {
                Project project = repository.findFirstByNumber(num);
                if (project != null) {
                    boolean flag = true;
                    for (Material material : project.getMaterials()) {
                        if (material.getStageType() == StageType.FINALS) {
                            flag = false;
                            int index = path.lastIndexOf(".");
                            if (index == -1) {
                                material.getDocPath().add(path);
                            } else {
                                String type = path.substring(index + 1);
                                if ("png".equalsIgnoreCase(type) || "jpeg".equalsIgnoreCase(type) || "jpg".equalsIgnoreCase(type)) {
                                    material.getImagePath().add(path);
                                } else if ("doc".equalsIgnoreCase(type) || "pdf".equalsIgnoreCase(type) || "docx".equalsIgnoreCase(type) || "mht".equalsIgnoreCase(type) ||
                                        "txt".equalsIgnoreCase(type) || "zip".equalsIgnoreCase(type) || "pages".equalsIgnoreCase(type) || "rtf".equalsIgnoreCase(type) ) {
                                    material.getDocPath().add(path);
                                } else if ("ppt".equalsIgnoreCase(type) || "pptx".equalsIgnoreCase(type)) {
                                    material.getPptPath().add(path);
                                } else if ("mp4".equalsIgnoreCase(type)) {
                                    material.getVideoPath().add(path);
                                } else {
                                    System.out.println(path + "   " + num);
                                }
                            }
                        }
                    }
                    if (flag) {
                        Material material = new Material();
                        material.setStageType(StageType.FINALS);
                        int index = path.lastIndexOf(".");
                        if (index == -1) {
                            material.getDocPath().add(path);
                        } else {
                            String type = path.substring(index + 1);
                            if ("png".equalsIgnoreCase(type) || "jpeg".equalsIgnoreCase(type) || "jpg".equalsIgnoreCase(type)) {
                                material.getImagePath().add(path);
                            } else if ("doc".equalsIgnoreCase(type) || "pdf".equalsIgnoreCase(type) || "docx".equalsIgnoreCase(type) || "mht".equalsIgnoreCase(type) ||
                                    "txt".equalsIgnoreCase(type) || "zip".equalsIgnoreCase(type) || "pages".equalsIgnoreCase(type) || "rtf".equalsIgnoreCase(type) ) {
                                material.getDocPath().add(path);
                            } else if ("ppt".equalsIgnoreCase(type) || "pptx".equalsIgnoreCase(type)) {
                                material.getPptPath().add(path);
                            } else if ("mp4".equalsIgnoreCase(type)) {
                                material.getVideoPath().add(path);
                            } else {
                                System.out.println(path + "   " + num);
                            }
                        }
                        project.getMaterials().add(material);
                    }
                } else {
                    System.out.println(num);
                }
                repository.save(project);
            }
        }
        return APIResult.success();
    }

    @PostMapping("data")
    public APIResult test(@RequestBody MultipartFile file) throws IOException, ByteException {
        Map<String, Project> map = new HashedMap<>();

        for (Row row : new XSSFWorkbook(file.getInputStream()).getSheetAt(0)) {
            if (row.getRowNum() == 0) {
                // 第一行 跳过
                continue;
            }


            //编号
            Cell cell0 = row.getCell(0);
            cell0.setCellType(CellType.STRING);
            String value0 = cell0.getStringCellValue();
            Project project = map.containsKey(value0) ? map.get(value0) : new Project();
            project.setId(UUIDUtil.getUUID());
            project.setNumber(value0);

            //名字
            Cell cell1 = row.getCell(1);
            cell1.setCellType(CellType.STRING);
            project.setName(cell1.getStringCellValue());

            //类别
            Cell cell2 = row.getCell(2);
            cell2.setCellType(CellType.STRING);
            String value2 = cell2.getStringCellValue();
            project.setCategoryId(categoryDao.findByName(value2).get(0).getId());


            //区域
            Cell cell8 = row.getCell(8);
            cell8.setCellType(CellType.STRING);
            String value8 = cell8.getStringCellValue();
            project.setAreaId(areaRepository.findByName(value8).getId());


            Member member = new Member();
            //学生名字
            Cell cell3 = row.getCell(3);
            cell3.setCellType(CellType.STRING);
            String value3 = cell3.getStringCellValue();
            member.setName(value3);
            member.setType(project.getMembers().size() > 0 ? MemberType.MEMBER : MemberType.LEADER);
            member.setAreaId(project.getAreaId());

            //身份证
            Cell cell4 = row.getCell(4);
            cell4.setCellType(CellType.STRING);
            String value4 = cell4.getStringCellValue();
            member.setIdCard(value4);
            member.setGender(IDCardUtil.getGenderByIdCard(value4));
            member.setBirthday(IDCardUtil.getYearByIdCard(value4) + "年" + IDCardUtil.getMonthByIdCard(value4) + "月" +
                    IDCardUtil.getDateByIdCard(value4) + "日");

            //电话
            Cell cell5 = row.getCell(5);
            cell5.setCellType(CellType.STRING);
            String value5 = cell5.getStringCellValue();
            member.setTelephone(value5);

            //组别
            Cell cell9 = row.getCell(9);
            cell9.setCellType(CellType.STRING);
            String value9 = cell9.getStringCellValue();
            project.setGroup(GroupType.getByName(value9));

            //学校
            Cell cell10 = row.getCell(10);
            cell10.setCellType(CellType.STRING);
            String value10 = cell10.getStringCellValue();
            String schoolId;
            if ("童程童美".equals(value10)) {
                schoolId = "b36d9bc2bd9e46699e885b21b2624249";
            } else if ("玉泉小学".equals(value10)) {
                schoolId = "8619566688684851a6bb2d7de0e69df8";
            } else {
                schoolId = schoolRepository.findByAreaIdAndName(project.getAreaId(), value10).get(0).getId();
            }
            School school = schoolRepository.findOne(schoolId);
            member.setGroup(school.getGroup());

            project.setSchoolId(schoolId);
            member.setSchoolId(schoolId);

            //年级
            Cell cell11 = row.getCell(11);
            cell11.setCellType(CellType.STRING);
            String value11 = cell11.getStringCellValue();
            member.setGrade(value11);

            //是否提交word
            Cell cell12 = row.getCell(12);
            cell12.setCellType(CellType.STRING);
            String value12 = cell12.getStringCellValue();
            project.setDocsubmitFlag("是".equals(value12));

            //是否最终提交
            Cell cell13 = row.getCell(13);
            cell13.setCellType(CellType.STRING);
            String value13 = cell13.getStringCellValue();
            project.setSubmitFlag("是".equals(value13));

            //获奖等级
            Cell cell14 = row.getCell(14);
            cell14.setCellType(CellType.STRING);
            String value14 = cell14.getStringCellValue();
            project.setPrize(value14);

            //指导老师
            Cell cell15 = row.getCell(15);
            cell15.setCellType(CellType.STRING);
            String value15 = cell15.getStringCellValue();

            Cell cell16 = row.getCell(16);
            cell16.setCellType(CellType.STRING);
            String value16 = cell16.getStringCellValue();

            String[] split15 = value15.split("、");
            String[] split16 = value16.split("、");

            List<Teacher> list = new ArrayList<>();
            for (int i = 0; i < (split15.length > split16.length ? split16.length : split15.length); i++) {
                Teacher teacher = new Teacher();
                teacher.setName(split15[i]);
                teacher.setTelephone(split16[i]);
                teacher.setNumber(i + 1);
                list.add(teacher);
            }

            //寄送地址
            Cell cell17 = row.getCell(17);
            cell17.setCellType(CellType.STRING);
            String value17 = cell17.getStringCellValue();
            project.setAddress(value17);


            //寄送联系方式
            Cell cell18 = row.getCell(18);
            cell18.setCellType(CellType.STRING);
            String value18 = cell18.getStringCellValue();
            project.setTelephone(value18);


            //创建时间
            Cell cell19 = row.getCell(19);
            cell19.setCellType(CellType.STRING);
            String value19 = cell19.getStringCellValue();
            project.setCreateTime(Objects.requireNonNull(StringUtil.getTime(value19)).getTime());
            project.setSignUpTime(Objects.requireNonNull(StringUtil.getTime(value19)).getTime());


            //提交时间
            Cell cell20 = row.getCell(20);
            cell20.setCellType(CellType.STRING);
            String value20 = cell20.getStringCellValue();
            List<Long> time = new ArrayList<>();
            if (EmptyUtil.isNotEmpty(value20)) {
                time.add(Objects.requireNonNull(StringUtil.getTime(value20)).getTime());
                project.setSubmitTime(time);
            }

            if (project.getTeachers().size() == 0) {
                project.setTeachers(list);
            }
            project.getMembers().add(member);


            Map<SubmitType, Boolean> submitStatus = new HashedMap<>();
            submitStatus.put(BASIS_MSG, true);
            submitStatus.put(PROJECT_BASIS_MSG, true);
            submitStatus.put(TEACHER_MSG, true);
            submitStatus.put(PRELIMINARY, false);
            submitStatus.put(REMATCH, false);
            submitStatus.put(FINALS, false);
            submitStatus.put(END, false);
            project.setSubmitStatus(submitStatus);

            project.setEliminateFlag(false);

            project.setCount(project.getMembers().size());
            map.put(value0, project);
        }
        map.forEach((x, y) -> repository.save(y));
        return APIResult.success();
    }

    @GetMapping("path")
    public APIResult path(@RequestParam String path) throws Exception {
        File pfile = new File(path);
        List<Project> list = new ArrayList<>();

        if (pfile.isDirectory()) {
            for (File file0 : pfile.listFiles()) {
                for (File file : file0.listFiles()) {
                    for (File file1 : file.listFiles()) {
                        for (File file2 : file1.listFiles()) {
                            for (File file3 : file2.listFiles()) {
                                for (File file4 : file3.listFiles()) {
                                    //项目编号
                                    String num = file4.getName().split("_")[1];
                                    Project project = repository.findFirstByNumber(num);
                                    Map<SubmitType, Boolean> submitStatus = project.getSubmitStatus();
                                    for (File file5 : file4.listFiles()) {
                                        StageType stageType = StageType.findByName(file5.getName());
                                        submitStatus.put(SubmitType.valueOf(stageType.name()), true);
                                        Material material = new Material();
                                        material.setStageType(stageType);
                                        for (File data : file5.listFiles()) {
                                            String filePath = upload(data);
                                            String name = data.getName();
                                            if (checkImage(name)) {
                                                material.getImagePath().add(filePath);
                                            } else if (checkppt(name)) {
                                                material.getPptPath().add(filePath);
                                            } else if (checkVideo(name)) {
                                                material.getVideoPath().add(filePath);
                                            } else if (checkWord(name)) {
                                                material.getDocPath().add(filePath);
                                            } else {
                                                System.out.println(name);
                                                throw new Exception();
                                            }
                                        }

                                        project.getMaterials().add(material);
                                        project.setSubmitStatus(submitStatus);
                                        list.add(project);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        log.info("" + list.size());
        list.forEach(project -> log.info(project.getNumber()));
        repository.save(list);
        return APIResult.success();
    }

    public String upload(File file) throws IOException {
        InputStream in = new FileInputStream(file);
        String fileName = file.getName();
        if (fileName.contains("\\")) {
            fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
        }
        String dataPath = "/" + UUIDUtil.getUUID() + "/" + fileName;

        String path = FileUtil.getProjectPath() + dataPath;

        log.info("文件保存路径++" + path);
        File saveFile = new File(path);
        if (saveFile.exists() && saveFile.isFile()) {
            saveFile.delete();
            saveFile = new File(path);
        }

        // 判断路径是否存在,如果不存在就创建文件路径
        if (!saveFile.getParentFile().exists()) {
            final boolean mkdirs = saveFile.getParentFile().mkdirs();
        }

        // 将上传文件保存到一个目标文件当中
        OutputStream out = new FileOutputStream(saveFile);

        byte[] bytes = new byte[1024];
        int num;
        while ((num = in.read(bytes)) != -1) {
            out.write(bytes, 0, num);
        }
        in.close();
        out.close();
        return "/uploads" + dataPath;
    }

    public boolean checkppt(String name) {
        String type = name.substring(name.lastIndexOf(".") + 1);
        if ("ppt".equalsIgnoreCase(type) || "pptx".equalsIgnoreCase(type)) {
            return true;
        }
        return false;
    }

    public boolean checkImage(String name) {
        String type = name.substring(name.lastIndexOf(".") + 1);
        if ("BMP".equalsIgnoreCase(type) || "JPG".equalsIgnoreCase(type) || "JPEG".equalsIgnoreCase(type) || "PNG".equalsIgnoreCase(type) || "GIF".equalsIgnoreCase(type)
                ) {
            return true;
        }
        return false;
    }

    public boolean checkVideo(String name) {
        String type = name.substring(name.lastIndexOf(".") + 1);
        if ("AVI".equalsIgnoreCase(type) || "rm".equalsIgnoreCase(type) || "rmvb".equalsIgnoreCase(type) || "MPEG".equalsIgnoreCase(type) || "MPG".equalsIgnoreCase(type
        ) || "DAT".equalsIgnoreCase(type) || "MOV".equalsIgnoreCase(type) || "mp4".equalsIgnoreCase(type) || "QT".equalsIgnoreCase(type) || "ASF".equalsIgnoreCase(type) ||
                "WMV".equalsIgnoreCase(type)
                ) {
            return true;
        }
        return false;
    }

    public boolean checkWord(String name) {
        String type = name.substring(name.lastIndexOf(".") + 1);
        if ("dot".equalsIgnoreCase(type) || "doc".equalsIgnoreCase(type) || "docx".equalsIgnoreCase(type) || "docm".equalsIgnoreCase(type) || "pdf".equalsIgnoreCase(type)
                ) {
            return true;
        }
        return false;
    }
}
