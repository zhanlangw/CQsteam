package cn.bytecloud.steam.project.service;

import cn.bytecloud.steam.area.entity.Area;
import cn.bytecloud.steam.area.service.AreaService;
import cn.bytecloud.steam.base.dto.PageModel;
import cn.bytecloud.steam.category.entity.*;
import cn.bytecloud.steam.category.service.CategoryService;
import cn.bytecloud.steam.constant.ModelConstant;
import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.exception.ErrorCode;
import cn.bytecloud.steam.file.service.FileService;
import cn.bytecloud.steam.project.dao.ProjectDao;
import cn.bytecloud.steam.project.dao.ProjectRepository;
import cn.bytecloud.steam.project.dto.*;
import cn.bytecloud.steam.project.entity.*;
import cn.bytecloud.steam.stats.dto.StatsDto;
import cn.bytecloud.steam.user.entity.UserType;
import cn.bytecloud.steam.user.service.UserService;
import cn.bytecloud.steam.user.thread.SMSHandler;
import cn.bytecloud.steam.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static cn.bytecloud.steam.category.entity.ParameterSetting.*;
import static cn.bytecloud.steam.category.entity.StageType.PRELIMINARY;
import static cn.bytecloud.steam.project.entity.SubmitType.*;

@Service
@Slf4j
public class ProjectServieImpl implements ProjectService {
    @Autowired
    private ProjectDao dao;

    @Autowired
    private AreaService areaService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProjectRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private SMSHandler handler;

    @Override
    public List<Project> findByCategoryId(final String categoryId) {
        return dao.findByCategoryId(categoryId);
    }

    @Override
    public Object updBasis(UpdBasisDto dto) throws ByteException {
        Project project = repository.findOne(dto.getId());
        Category category = categoryService.findOne(project.getCategoryId());
        if (category.getStages().get(0).getEndTime() < System.currentTimeMillis() && UserUtil.getUser().getType() == UserType.USER) {
            throw new ByteException(ErrorCode.FAILURE, "禁止修改");
        }

        if (UserUtil.getUser().getType() == UserType.USER) {
            if (project.isSubmitFlag() || category.getSignUpTime() > System.currentTimeMillis()) {
                throw new ByteException(ErrorCode.AUTHORIZATION);
            }

            if (category.getStages().get(0).getEndTime() < System.currentTimeMillis()) {
                throw new ByteException(ErrorCode.AUTHORIZATION);
            }
        }


        if (category.getMaxMember() < dto.getCount() || category.getMinMember() > dto.getCount() || dto.getCount() !=
                dto.getMembers().size()) {
            throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"count"});
        }

        if (dto.getMembers().stream().map(AddBasisMemberDto::getIdCard).distinct().collect(Collectors.toSet()).size()
                != dto
                .getMembers().size()) {
            throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"身份证号码重复"});
        }
        if (dto.getMembers().stream().map(AddBasisMemberDto::getName).distinct().collect(Collectors.toSet()).size()
                != dto
                .getMembers().size()) {
            throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"名字重复"});
        }
        if (dto.getMembers().stream().map(AddBasisMemberDto::getTelephone).distinct().collect(Collectors.toSet()).size()
                != dto
                .getMembers().size()) {
            throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"电话重复"});
        }

        project = dto.toData(project);
        project = dao.save(project);
        userService.save(dto.getMembers().stream().map(AddBasisMemberDto::getIdCard).collect(Collectors.toSet()));
        return project.toBasisDto();
    }

    @Override
    public Object importFile(String id, Integer stage, Integer fileType, HttpServletRequest request) throws
            FileUploadException,
            ByteException, IOException {
        Project project = repository.findOne(id);
        Category category = categoryService.findOne(project.getCategoryId());
        return fileService.saveFileEntity(request, fileType);
    }

    @Override
    public void delFile(String path) throws ByteException {
        fileService.deleteFile(path);
    }

    @Override
    public List<HashMap> myProject() throws ByteException {
        List<HashMap> list = dao.myProject();
        for (HashMap map : list) {
            Project project = repository.findOne((String) map.get("id"));
            Category category = categoryService.findOne(project.getCategoryId());
            Map<SubmitType, Boolean> submitStatus = project.getSubmitStatus();

            //淘汰就按照提交状态确定进度
            if (project.isEliminateFlag()) {
                int max = submitStatus.keySet().stream().mapToInt(SubmitType::getEnumType).summaryStatistics().getMax();
                if (max < 4) {
                    if (project.isSubmitFlag()) {
                        map.put("stage", "初赛");
                    } else {
                        map.put("stage", "报名中");
                    }
                } else {
                    map.put("stage", SubmitType.getType(max).getEnumValue());
                }
                continue;
            }

            //没有淘汰根据时间确定进度
            //max 对应 SubmitType 的type数字
            int max = 1;
            if (!(submitStatus.get(BASIS_MSG) || submitStatus.get(PROJECT_BASIS_MSG))) {
                max = 1;
            }
            if (category.getParamSetting().contains(ParameterSetting.GUIDE_TEACHER) && !(submitStatus.get
                    (TEACHER_MSG))) {
                max = 1;
            }

            boolean flag = true;
            List<Stage> stages = category.getStages();
            StageType stageType = stages.get(stages.size() - 1).getType();

            if (flag) {
                for (Stage stage : category.getStages()) {
                    if (stage.getType() == PRELIMINARY) {
                        max = 4;
                        if (stage.getEndTime() > System.currentTimeMillis()) {
                            flag = false;
                            break;
                        } else {
                            if (stageType == PRELIMINARY) {
                                max = 7;
                            }
                        }
                        break;
                    }
                }
            }
            if (flag) {
                for (Stage stage : category.getStages()) {
                    if (stage.getType() == StageType.REMATCH) {
                        max = 5;
                        if (stage.getEndTime() > System.currentTimeMillis()) {

                            flag = false;
                        } else {
                            if (stageType == StageType.REMATCH) {
                                max = 7;
                            }
                        }
                        break;
                    }
                }
            }
            if (flag) {
                for (Stage stage : category.getStages()) {
                    if (stage.getType() == StageType.FINALS) {
                        if (stage.getEndTime() > System.currentTimeMillis()) {
                            max = 6;
                        } else {
                            max = 7;
                        }
                        break;
                    }
                }
            }

            if (max < 4) {
                if (project.isSubmitFlag()) {
                    map.put("stage", "初赛");
                } else {
                    map.put("stage", "报名中");
                }
            } else {

                map.put("stage", SubmitType.getType(max).getEnumValue());
            }

//            Map<String, Boolean> submitStage = (Map<String, Boolean>) map.remove("submitStage");
//            List<Map<String, Object>> stageList = (List<Map<String, Object>>) map.remove("categoryStage");
//            for (int i = 0; i < stageList.size(); i++) {
//
//                if (!(submitStage.get(BASIS_MSG.name()) && submitStage.get(PROJECT_BASIS_MSG.name()))) {
//                    map.put("stage", "报名中");
//                    break;
//                }
//
//                if (submitStage.containsKey(SubmitType.TEACHER_MSG.name()) && (!submitStage.get(SubmitType
//                        .TEACHER_MSG.name()))) {
//                    map.put("stage", "报名中");
//                    break;
//                }
//
//                Map<String, Object> stageMap = stageList.get(i);
//                String type = (String) stageMap.get(CATEGORY_STAGE_TYPE);
//                if (type.equals(PRELIMINARY.name())) {
//                    if ((submitStage.containsKey(PRELIMINARY.name()) && !submitStage.get(PRELIMINARY.name())) ||
//                            !submitStage.containsKey(PRELIMINARY.name())) {
//                        map.put("stage", "报名中");
//                        break;
//                    }
//                }
//                if (type.equals(REMATCH.name())) {
//                    if ((submitStage.containsKey(REMATCH.name()) && !submitStage.get(REMATCH.name())) ||
//                            !submitStage.containsKey(REMATCH.name())) {
//                        if (i == 0) {
//                            map.put("stage", "报名中");
//                        } else {
//                            map.put("stage", "初赛");
//                        }
//                        break;
//                    }
//                }
//                if (type.equals(FINALS.name())) {
//                    if ((submitStage.containsKey(FINALS.name()) && !submitStage.get(FINALS.name())) ||
//                            !submitStage.containsKey(FINALS.name())) {
//                        if (i == 0) {
//                            map.put("stage", "报名中");
//                        } else if (i == 1) {
//                            map.put("stage", "初赛");
//                        } else {
//                            map.put("stage", "复赛");
//                        }
//                        break;
//                    } else {
//                        map.put("stage", "决赛");
//                    }
//                }
//                map.put("stage", "已结束");
//            }
        }
        return list;
    }

    @Override
    public Object list(ProjectPageDto dto) {
        return dao.list(dto);
    }

    @Override
    public void export(ProjectPageDto dto, HttpServletResponse response, HttpServletRequest request) throws
            IOException {
        Vector<Vector<String>> rowName = new Vector<>();
        for (HashMap item : dao.getExportData(dto)) {
            Vector<String> row = new Vector<>();
            row.add((String) item.get("number"));
            row.add((String) item.get("name"));
            row.add((String) item.get("category"));
            row.add((String) item.get("memberName"));
            row.add((String) item.get("idCard"));
            row.add((String) item.get("memberTelephone"));
            row.add((String) item.get("province"));
            row.add((String) item.get("city"));
            row.add((String) item.get("district"));
            row.add((String) item.get("group"));
            row.add((String) item.get("school"));
            row.add((String) item.get("grade"));
            row.add(((boolean) item.get("docFlag")) == false ? "否" : "是");
            row.add(((boolean) item.get("submitFlag")) == false ? "否" : "是");
            row.add((String) item.get("prize"));

            List<HashMap> teachers = (List<HashMap>) item.get("teachers");
            HashMap teacher = null;
            StringBuilder teacherName = new StringBuilder();
            StringBuilder teacherTelephon = new StringBuilder();
            for (HashMap hashMap : teachers) {
                int levle = (int) hashMap.get(ModelConstant.PROJECT_TEACHER_LEVLE);
                teacher = hashMap;
                teacherName.append(" ").append(teacher.get(ModelConstant.PROJECT_TEACHER_NAME));
                teacherTelephon.append(" ").append(teacher.get(ModelConstant.PROJECT_TEACHER_TELEPHONE));
            }
            row.add(teacherName.toString().trim().replaceAll(" ", "、"));
            row.add(teacherTelephon.toString().trim().replaceAll(" ", "、"));

            row.add((String) item.get("address"));
            row.add((String) item.get("telephone"));

            row.add((String) item.get("createTime"));
            row.add((String) item.get("submitTime"));
            rowName.add(row);
        }

        //设置行名 Vector<String>
        Vector<String> rowTopName = new Vector<String>();
        rowTopName.add("编号");
        rowTopName.add("名称 ");
        rowTopName.add("赛项");
        rowTopName.add("学生名字");
        rowTopName.add("身份证号码");
        rowTopName.add("手机号码");
        rowTopName.add("省");
        rowTopName.add("市");
        rowTopName.add("区");
        rowTopName.add("组别");
        rowTopName.add("学校");
        rowTopName.add("年级");
        rowTopName.add("是否提交word");
        rowTopName.add("是否已最终提交");
        rowTopName.add("获奖等级");
        rowTopName.add("指导老师名字");
        rowTopName.add("指导老师电话");
        rowTopName.add("奖状寄送地址");
        rowTopName.add("奖状寄送联系方式");
        rowTopName.add("创建时间");
        rowTopName.add("提交时间");

        String fileName = "项目数据_" + StringUtil.getExportTime();

        ServletOutputStream out = response.getOutputStream();

        String userAgent = request.getHeader("user-agent");
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {//IE内核
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } else {//非IE内核
            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
        }

        fileName += ".xlsx";
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/download");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        ExcelUtil.exportToExcelXSSF("sheet", rowTopName, rowName, out);

        out.flush();
        out.close();
    }

    @Override
    public void sendMsg(ProjectPageDto dto) {
        PageModel<HashMap> pageModel = dao.list(dto);
        List<HashMap> list = pageModel.getValue();
        list.stream().map(item -> (String) (item.get("telephone"))).collect(Collectors.toList()).forEach
                (item -> handler.getInstance().addCaptchaMsg(item, null));
    }

    @Override
    public Object importResult(FileItem file, String categoryId, Integer stage) throws IOException, ByteException {
        String type = file.getName().substring(file.getName().lastIndexOf(".") + 1);

        Workbook workbook = null;
        if ("xls".equalsIgnoreCase(type)) {
            workbook = new HSSFWorkbook(file.getInputStream());
        } else if ("xlsx".equalsIgnoreCase(type)) {
            workbook = new XSSFWorkbook(file.getInputStream());
        } else {
            throw new ByteException(ErrorCode.FAILURE, "文件类型错误");
        }
        Category category = categoryService.findOne(categoryId);

        AtomicBoolean docFlag = new AtomicBoolean(true);

        //判断导出阶段是否正确
        if (!Category.getBeforeStatus(category).equals(stage)) {
            throw new ByteException(ErrorCode.FAILURE, "阶段错误！");
        }

        category.getStages().stream().filter(item -> item.getType().getEnumType() > stage).min(Comparator.comparing
                (Stage::getType)).ifPresent(item -> {
            docFlag.set(!(item.getDocNum() > 0));
        });

        IntSummaryStatistics statistics = category.getStages().stream().map(Stage::getType).mapToInt
                (StageType::getEnumType).summaryStatistics();

        boolean prizeFlag = statistics.getMax() == stage;
        List<Project> list = checkSheet(workbook.getSheetAt(0), category, prizeFlag);
        list.forEach(project -> {
            if (!prizeFlag) {
                if (category.getStages().size() == 2) {
                    project.getSubmitStatus().put(SubmitType.valueOf(StageType.getType(statistics.getMax()).name()),
                            false);
                    project.setStage(StageType.getType(statistics.getMax()));
                } else {
                    if (stage != statistics.getMin()) {
                        project.getSubmitStatus().put(SubmitType.valueOf(StageType.getType(statistics.getMax()).name()),
                                false);
                        project.setStage(StageType.getType(statistics.getMax()));
                    } else {
                        project.getSubmitStatus().put(SubmitType.REMATCH, false);
                        project.setStage(StageType.REMATCH);
                    }
                }
                project.setSubmitFlag(false);
                project.setEliminateFlag(false);
                project.setDocsubmitFlag(docFlag.get());
            }
            repository.save(project);
        });
        List<String> ids = list.stream().map(Project::getId).collect(Collectors.toList());
        repository.findByCategoryId(categoryId).stream().filter(item -> !ids.contains(item.getId())).collect(Collectors.toList()).forEach(project -> {
            project.setEliminateFlag(true);
            repository.save(project);
        });
        return null;
    }

    @Override
    public List<Project> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Project> findByDocsubmitFlag(boolean flag) {
        return repository.findByDocsubmitFlag(flag);
    }

    @Override
    public void delFile(String path, Integer stage, String id, Integer fileType) throws ByteException {
        fileService.deleteFile(path);
        Project project = repository.findOne(id);
        Category category = categoryService.findOne(project.getCategoryId());
        Integer status = Category.getStatus(category) - 1;

        for (Material material : project.getMaterials()) {
            if (material.getStageType().getEnumType().equals(stage)) {
                switch (fileType) {
                    case 1:
                        material.getPptPath().remove(path);
                        break;
                    case 2:
                        material.getDocPath().remove(path);
                        if (material.getDocPath().size() == 0 && stage.equals(status)) {
                            project.setDocsubmitFlag(false);
                        }
                        break;
                    case 3:
                        material.getVideoPath().remove(path);
                        break;
                    case 4:
                        material.getImagePath().remove(path);
                        break;
                    default:
                        throw new ByteException(ErrorCode.PARAMETER_ERROR, "文件类型错误!");

                }
                break;
            }
        }
        project.getSubmitStatus().put(SubmitType.valueOf(StageType.getType(stage).name()), false);
        repository.save(project);
    }

    @Override
    public List<Project> findByDate(Date date, String field) {
        return dao.findByDate(date, field);
    }

    @Override
    public List<HashMap> signUpStats(StatsDto dto) {
        return dao.signUpStats(dto);
    }

    @Override
    public List<HashMap> wordStats(StatsDto dto) {
        return dao.wordStats(dto);
    }

    @Override
    public Long wordCount() {
        return repository.findAll().stream().mapToLong(item ->
                item.getMaterials().stream().mapToLong(material -> material.getPptPath().size() + material.getDocPath().size()).sum()
        ).reduce(0L, (x, y) -> x + y);
    }

    @Override
    public List<HashMap> submitStats(StatsDto dto) {
        return dao.submitStats(dto);
    }

    @Override
    public Integer submitCount() {
        return repository.findAll().stream().mapToInt(item -> item.getSubmitTime().size()).reduce(0, (x, y) -> x + y);
    }

    @Override
    public Map[] prizeStats(StatsDto dto) {
        Map[] data = new Map[4];
        List<HashMap> list = dao.prizeStats(dto);
        list.forEach(map -> {
            String prize = (String) map.get("prize");
            if ("一等奖".equals(prize)) {
                data[0] = map;
            }
            if ("二等奖".equals(prize)) {
                data[1] = map;
            }
            if ("三等奖".equals(prize)) {
                data[2] = map;
            }
            if ("优秀奖".equals(prize)) {
                data[3] = map;
            }
        });
        for (int i = 0; i < data.length; i++) {
            if (data[i] == null) {
                Map map = new HashMap();
                map.put("count", 0);
                switch (i) {
                    case 0:
                        map.put("prize", "一等奖");
                        data[i] = map;
                        break;
                    case 1:
                        map.put("prize", "二等奖");
                        data[i] = map;
                        break;
                    case 2:
                        map.put("prize", "三等奖");
                        data[i] = map;
                        break;
                    case 3:
                        map.put("prize", "优秀奖");
                        data[i] = map;
                        break;
                    default:
                        break;
                }
            }
        }
        return data;
    }

    @Override
    public void del(String id) throws ByteException {
        Project project = repository.findOne(id);
        List<String> list = new ArrayList<>();
        list.add(project.getCreatorId());
        project.getMembers().forEach(member -> list.add(member.getIdCard()));
        if ((!list.contains(UserUtil.getUserId()) && (!list.contains(UserUtil.getUser().getUsername())))) {
            throw new ByteException(ErrorCode.FAILURE, "只能删除自己的项目");
        }
        repository.delete(id);
        delProjectFile(project);
    }

    @Override
    public void adminDel(String id) {
        Project project = repository.findOne(id);
        repository.delete(id);
        delProjectFile(project);
    }

    @Override
    public void exportStudent(HttpServletResponse response, HttpServletRequest request) throws IOException {
        Vector<Vector<String>> rowName = new Vector<>();
        List<HashMap> data = dao.getExportStudent();
        for (HashMap item : data) {
            Vector<String> row = new Vector<>();
//            row.add((String) item.get("id"));
            row.add((String) item.get("name"));
            row.add((String) item.get("number"));
            row.add((String) item.get("telephone"));
            row.add((String) item.get("idcard"));
            row.add((String) item.get("password"));
            row.add(GroupType.valueOf((String) item.get("group")).getEnumValue());
            row.add((String) item.get("schoolId"));
            row.add((String) item.get("schoolName"));
            rowName.add(row);
        }

        //设置行名 Vector<String>
        Vector<String> rowTopName = new Vector<String>();
//        rowTopName.add("ID");
        rowTopName.add("名字");
        rowTopName.add("考号");
        rowTopName.add("电话 ");
        rowTopName.add("身份证");
        rowTopName.add("密码");
        rowTopName.add("组别");
        rowTopName.add("学校ID");
        rowTopName.add("学校名称");

        String fileName = "报名数据_" + StringUtil.getExportTime();

        ServletOutputStream out = response.getOutputStream();

        String userAgent = request.getHeader("user-agent");
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {//IE内核
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } else {//非IE内核
            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
        }

        fileName += ".xlsx";
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/download");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        ExcelUtil.exportToExcelXSSF("sheet", rowTopName, rowName, out);

        out.flush();
        out.close();
    }

    @Override
    public void exportSchool(HttpServletResponse response, HttpServletRequest request) throws IOException {
        Vector<Vector<String>> rowName = new Vector<>();
        List<HashMap> data = dao.getExportSchool();
        for (HashMap item : data) {
            Vector<String> row = new Vector<>();
            row.add((String) item.get("id"));
            row.add((String) item.get("name"));
            row.add((String) item.get("province"));
            row.add((String) item.get("city"));
            row.add((String) item.get("district"));
            row.add((String) item.get("districtId"));
            rowName.add(row);
        }

        //设置行名 Vector<String>
        Vector<String> rowTopName = new Vector<String>();
        rowTopName.add("学校ID");
        rowTopName.add("学校名称");
        rowTopName.add("省 ");
        rowTopName.add("市");
        rowTopName.add("区");
        rowTopName.add("区ID(和学校关联)");

        String fileName = "学校数据_" + StringUtil.getExportTime();

        ServletOutputStream out = response.getOutputStream();

        String userAgent = request.getHeader("user-agent");
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {//IE内核
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } else {//非IE内核
            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
        }

        fileName += ".xlsx";
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/download");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        ExcelUtil.exportToExcelXSSF("sheet", rowTopName, rowName, out);

        out.flush();
        out.close();
    }

    @Override
    public void exportArea(HttpServletResponse response, HttpServletRequest request) throws IOException {
        Vector<Vector<String>> rowName = new Vector<>();
        List<Area> data = areaService.getExportArea();
        for (Area area : data) {
            Vector<String> row = new Vector<>();
            row.add(area.getId());
            row.add(area.getName());
            rowName.add(row);
        }

        //设置行名 Vector<String>
        Vector<String> rowTopName = new Vector<String>();
        rowTopName.add("ID");
        rowTopName.add("名称");

        String fileName = "区域数据_" + StringUtil.getExportTime();

        ServletOutputStream out = response.getOutputStream();

        String userAgent = request.getHeader("user-agent");
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {//IE内核
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } else {//非IE内核
            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
        }

        fileName += ".xlsx";
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/download");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        ExcelUtil.exportToExcelXSSF("sheet", rowTopName, rowName, out);

        out.flush();
        out.close();
    }

    public void delProjectFile(Project project) {
        project.getMaterials().forEach(material -> {
            delFlile(material.getDocPath());
            delFlile(material.getPptPath());
            delFlile(material.getVideoPath());
            delFlile(material.getImagePath());
        });
    }

    private void delFlile(List<String> paths) {
        paths.forEach(path -> {
            try {
                delFile(path);
            } catch (ByteException e) {
                log.info("删除项目材料失败,材料路径:" + path);
                e.printStackTrace();
            }
        });
    }

    private List<Project> checkSheet(Sheet sheet, Category category, boolean flag) throws ByteException {
        List<Project> list = new ArrayList<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }
            if (row.getCell(0) == null) {
                continue;
            }
            String rowNum = (row.getRowNum() + 1) + "";

            Cell cell = row.getCell(0);
            cell.setCellType(CellType.STRING);
            String number = cell.getStringCellValue();
            if (EmptyUtil.isEmpty(number)) {
                continue;
            }
            String prize = null;
            if (flag) {
                cell = row.getCell(1);
                if (cell == null) {
                    throw new ByteException(ErrorCode.IMPORT_ERROR, new String[]{rowNum, "获奖等级为空"});
                }
                cell.setCellType(CellType.STRING);
                prize = cell.getStringCellValue();
                if (EmptyUtil.isEmpty(prize)) {
                    throw new ByteException(ErrorCode.IMPORT_ERROR, new String[]{rowNum, "获奖等级为空"});
                }
            }

            Project project = repository.findFirstByNumber(number);
            if (null == project) {
                throw new ByteException(ErrorCode.IMPORT_ERROR, new String[]{rowNum, "项目编号为空"});
            }
//            if (!project.isSubmitFlag()) {
//                throw new ByteException(ErrorCode.IMPORT_ERROR, new String[]{rowNum, "项目没有最终提交"});
//            }
            project.setPrize(prize);
            list.add(project);
        }
        return list;
    }


    @Override
    public Object addBasis(AddBasisDto dto) throws ByteException {
        Category category = categoryService.findOne(dto.getCategoryId());

        if (UserUtil.getUser().getType() == UserType.USER) {
            if (category.getSignUpTime() > System.currentTimeMillis()) {
                throw new ByteException(ErrorCode.AUTHORIZATION);
            }
        }
        if (category.getStages().get(0).getEndTime() < System.currentTimeMillis()) {
            throw new ByteException(ErrorCode.FAILURE, "报名已结束");
        }

        if (category.getMaxMember() < dto.getCount() || category.getMinMember() > dto.getCount() || dto.getCount() !=
                dto.getMembers().size()) {
            throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"count"});
        }

        if (dto.getMembers().stream().map(AddBasisMemberDto::getIdCard).distinct().collect(Collectors.toSet()).size()
                != dto
                .getMembers().size()) {
            throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"身份证号码重复"});
        }
        if (dto.getMembers().stream().map(AddBasisMemberDto::getName).distinct().collect(Collectors.toSet()).size()
                != dto
                .getMembers().size()) {
            throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"名字重复"});
        }
        if (dto.getMembers().stream().map(AddBasisMemberDto::getTelephone).distinct().collect(Collectors.toSet()).size()
                != dto
                .getMembers().size()) {
            throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"电话重复"});
        }
        String year = Calendar.getInstance().get(Calendar.YEAR) + "";
        String number = category.getAbbreviation() + year.substring(year.length() - 2) + dto.getMembers().get(0)
                .getGroup();
        Project project = dto.toData(category);
        project.setNumber(number);

        Project save = dao.save(project);
        userService.save(dto.getMembers().stream().map(AddBasisMemberDto::getIdCard).collect(Collectors.toSet()));

        log.info("用户：【" + UserUtil.getUser().getUsername() + "】报名项目成功，项目编号：" + save.getNumber());
        return save.toBasisDto();
    }

    @Override
    public Object itemBasis(String id) {
        return repository.findOne(id).toBasisDto();
    }

    @Override
    public Object addBasisProject(AddBasisProjectDto dto) throws ByteException {
        Project project = repository.findOne(dto.getId());
        Category category = categoryService.findOne(project.getCategoryId());
        if (category.getStages().get(0).getEndTime() < System.currentTimeMillis() && UserUtil.getUser().getType() ==
                UserType.USER) {
            throw new ByteException(ErrorCode.FAILURE, "禁止修改");
        }

        return repository.save(checkDto(dto, project, category)).toBasisProjectDto(category);
    }

    @Override
    public Object itemBasisProject(String id) {
        Project project = repository.findOne(id);
        Category category = categoryService.findOne(project.getCategoryId());
        return project.toBasisProjectDto(category);
    }

    @Override
    public Object addTeacher(AddTeacherDto dto) throws ByteException {
        Project project = repository.findOne(dto.getId());
        Category category = categoryService.findOne(project.getCategoryId());
        if (category.getStages().get(0).getEndTime() < System.currentTimeMillis() && UserUtil.getUser().getType() == UserType.USER) {
            throw new ByteException(ErrorCode.FAILURE, "禁止修改");
        }


        if (UserUtil.getUser().getType() == UserType.USER) {
            if (project.isSubmitFlag() || category.getSignUpTime() > System.currentTimeMillis()) {
                throw new ByteException(ErrorCode.AUTHORIZATION);
            }
        }


        for (int i = 0; i < dto.getTeachers().size(); i++) {
            TeacherDto teacherDto = dto.getTeachers().get(i);
            if (category.getParamSetting().contains(ParameterSetting.SUBJECT)) {
                if (EmptyUtil.isEmpty(teacherDto.getSubject())) {
                    throw new ByteException(ErrorCode.NULL_PARAMETER, new String[]{"teachers[" + i + "]subject"});
                }
            }
            if (!MatchUtil.checkTelephone(teacherDto.getTelephone())) {
                throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"telephone"});
            }
        }

        project.setTeachers(dto.getTeachers().stream().map(item -> item.toData(category)).collect(Collectors
                .toList()));
        project.getSubmitStatus().put(TEACHER_MSG, true);
        return repository.save(project).toTeacherDto();
    }

    @Override
    public Object itemTeacher(String id) {
        return repository.findOne(id).toTeacherDto();
    }

    @Override
    public Object addMaterial(AddMaterialDto dto) throws ByteException {
        Project project = repository.findOne(dto.getId());
        Category category = categoryService.findOne(project.getCategoryId());

        List<Stage> stages = category.getStages();
        boolean flag = true;
        for (Stage stage : stages) {
            if (stage.getType() == dto.getStageType()) {
                flag = false;
                if (UserUtil.getUser().getType() == UserType.USER) {
                    if (stage.getEndTime() < System.currentTimeMillis() || project.isSubmitFlag()) {
                        throw new ByteException(ErrorCode.AUTHORIZATION);
                    }
                }

                if (stage.getPptNum() < dto.getPptPath().size()) {
                    throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"ppt size"});
                }
                if (stage.getDocNum() < dto.getDocPath().size()) {
                    throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"doc size"});
                }
                if (stage.getVideoNum() < dto.getVideoPath().size()) {
                    throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"video size"});
                }
                if (stage.getImageNum() < dto.getImagePath().size()) {
                    throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"image size"});
                }
            }
        }
        if (flag) {
            throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"stageType"});
        }
        flag = true;
        List<Material> materials = project.getMaterials();
        for (Material material : materials) {
            if (dto.getStageType() == material.getStageType()) {
                materials.remove(material);
                materials.add(dto.toData());
                flag = false;
                break;
            }
        }
        if (flag) {
            Material material = dto.toData();
            if (material.getDocPath().size() > 0) {
                project.getDocSubmitTime().add(System.currentTimeMillis());
                project.setDocsubmitFlag(true);
            }
            materials.add(material);
        }

        project.getSubmitStatus().put(SubmitType.valueOf(dto.getStageType().name()), true);

        return repository.save(project).toMaterialDto(dto.getStageType());
    }

    @Override
    public Object itemMaterial(String id, Integer stageType) {
        return repository.findOne(id).toMaterialDto(StageType.getType(stageType));
    }

    @Override
    public Object status(String id) {
        Project project = repository.findOne(id);
        Category category = categoryService.findOne(project.getCategoryId());
        Map map = new HashMap();
        map.put("id", id);
        Map<SubmitType, Boolean> submitStatus = project.getSubmitStatus();

        if (project.isEliminateFlag()) {
            submitStatus.forEach((key, value) -> {
                map.put(key.getEnumType() + "", value);
            });
            return map;
        }

        map.put("1", submitStatus.get(BASIS_MSG));
        map.put("2", submitStatus.get(PROJECT_BASIS_MSG));

        if (category.getParamSetting().contains(ParameterSetting.GUIDE_TEACHER)) {
            map.put("3", submitStatus.get(TEACHER_MSG));
        }

        boolean flag = true;

        if (flag) {
            for (Stage stage : category.getStages()) {
                if (stage.getType() == PRELIMINARY) {
                    if (submitStatus.get(SubmitType.PRELIMINARY) != null) {
                        map.put("4", submitStatus.get(SubmitType.PRELIMINARY));
                    }
                    if (stage.getEndTime() > System.currentTimeMillis()) {
                        flag = false;
                    }
                }
            }
        }
        if (flag) {
            for (Stage stage : category.getStages()) {
                if (stage.getType() == StageType.REMATCH) {
                    if (submitStatus.get(SubmitType.REMATCH) != null) {
                        map.put("5", submitStatus.get(SubmitType.REMATCH));
                    }
                    if (stage.getEndTime() > System.currentTimeMillis()) {
                        flag = false;
                    }
                }
            }
        }
        if (flag) {
            for (Stage stage : category.getStages()) {
                if (stage.getType() == StageType.FINALS) {
                    if (submitStatus.get(SubmitType.FINALS) != null) {
                        map.put("6", submitStatus.get(SubmitType.FINALS));
                    }
                    if (stage.getEndTime() > System.currentTimeMillis()) {
                        flag = false;
                    }
                }
            }
        }

        return map;
    }

    @Override
    public void submit(String id) throws ByteException {
        Project project = repository.findOne(id);
        Category category = categoryService.findOne(project.getCategoryId());

        Map<SubmitType, Boolean> submitStatus = project.getSubmitStatus();
        if (!submitStatus.get(BASIS_MSG)) {
            throw new ByteException(ErrorCode.FAILURE, "基础信息未提交");
        }

        if (!submitStatus.get(PROJECT_BASIS_MSG)) {
            throw new ByteException(ErrorCode.FAILURE, "项目基础信息");
        }

        if (category.getParamSetting().contains(ParameterSetting.GUIDE_TEACHER) && !submitStatus.get(
                TEACHER_MSG)) {
            throw new ByteException(ErrorCode.FAILURE, "指导老师信息未提交");
        }
        Set<SubmitType> set = submitStatus.keySet();

        if (set.contains(SubmitType.PRELIMINARY) && !submitStatus.get(SubmitType.PRELIMINARY)) {
            throw new ByteException(ErrorCode.FAILURE, "初赛资料未提交");

        }
        if (set.contains(SubmitType.REMATCH) && !submitStatus.get(SubmitType.REMATCH)) {
            throw new ByteException(ErrorCode.FAILURE, "复赛资料未提交");

        }
        if (set.contains(SubmitType.FINALS) && !submitStatus.get(SubmitType.FINALS)) {
            throw new ByteException(ErrorCode.FAILURE, "决赛资料未提交");

        }
        //去掉提交判断判断
//        int max = set.stream().mapToInt(SubmitType::getEnumType).summaryStatistics().getMax();
//
//        StageType stageType = StageType.valueOf(SubmitType.getType(max).name());
//
//        boolean flag = true;
//        for (Stage stage : category.getStages()) {
//            if (stage.getType() == stageType && stage.getEndTime() < System.currentTimeMillis()) {
//                throw new ByteException(ErrorCode.FAILURE, stage.getType().getEnumValue() + "提交时间已经结束");
//            } else {
//                flag = false;
//            }
//        }
//        if (flag) {
//            throw new ByteException(ErrorCode.FAILURE);
//        }

        dao.submit(id);
    }


    private Project checkDto(AddBasisProjectDto dto, Project project, Category category) throws ByteException {
        if (UserUtil.getUser().getType() == UserType.USER) {
            if (project.isSubmitFlag() || category.getSignUpTime() > System.currentTimeMillis()) {
                throw new ByteException(ErrorCode.AUTHORIZATION);
            }
        }

        if (!MatchUtil.checkTelephone(dto.getTelephone())) {
            throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"telephone"});
        }

        if (project.getMembers().size() != dto.getMembers().size()) {
            throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"members"});
        }

        List<ParameterSetting> paramSetting = category.getParamSetting();
        if (paramSetting.contains(NAME_AND_DESC)) {
            if (EmptyUtil.isEmpty(dto.getName()) || EmptyUtil.isEmpty(dto.getDesc())) {
                throw new ByteException(ErrorCode.NULL_PARAMETER, new String[]{"name or desc"});

            } else {
                project.setName(dto.getName());
                project.setDesc(dto.getDesc());
            }
        }


        for (int i = 0; i < dto.getMembers().size(); i++) {
            AddBasisProjectMemberDto memberDto = dto.getMembers().get(i);
            Member member = project.getMembers().get(i);
            if (paramSetting.contains(MEMBER_LDENTITY)) {
                if (memberDto.getType() == null) {
                    throw new ByteException(ErrorCode.NULL_PARAMETER, new String[]{"type"});
                } else {
                    member.setType(memberDto.getType());
                }
            }

            if (paramSetting.contains(OTHER)) {
                if (EmptyUtil.isEmpty(memberDto.getPassport())) {
//                    throw new ByteException(ErrorCode.NULL_PARAMETER, new String[]{"passport"});
                } else {
                    member.setPassport(memberDto.getPassport());
                }

                Parent parent = memberDto.getParent();
                if (EmptyUtil.isEmpty(parent.getFatherName())) {
                    throw new ByteException(ErrorCode.NULL_PARAMETER, new String[]{"fatherName"});
                }
                if (EmptyUtil.isEmpty(parent.getMotherName())) {
                    throw new ByteException(ErrorCode.NULL_PARAMETER, new String[]{"motherName"});
                }
                if (EmptyUtil.isEmpty(parent.getFathertelephone()) || !MatchUtil.checkTelephone(parent
                        .getFathertelephone())) {
                    throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"fathertelephone"});
                }
                if (EmptyUtil.isEmpty(parent.getMothertelephone()) || !MatchUtil.checkTelephone(parent
                        .getMothertelephone())) {
                    throw new ByteException(ErrorCode.PARAMETER_ERROR, new String[]{"mothertelephone"});
                }
                if (EmptyUtil.isEmpty(memberDto.getAddress())) {
                    throw new ByteException(ErrorCode.NULL_PARAMETER, new String[]{"address"});
                } else {
                    member.setAddress(memberDto.getAddress());
                }

                member.setParent(parent);

            }
//            member.setGender(memberDto.getGender());
//            member.setBirthday(memberDto.getBirthday());
            member.setImagePath(memberDto.getImagepath());
            member.setGrade(memberDto.getGrade());
        }

        if (paramSetting.contains(MEMBER_LDENTITY)) {
            List<MemberType> collect = dto.getMembers().stream().map(AddBasisProjectMemberDto::getType)
                    .filter(item -> item == MemberType.LEADER).collect(Collectors.toList());
            if (collect.size() == 0) {
                throw new ByteException(ErrorCode.FAILURE, "必须要有一个成员是队长");
            }
            if (collect.size() > 1) {
                throw new ByteException(ErrorCode.FAILURE, "只能有一个成员是队长");
            }
        }
        project.setAddress(dto.getAddress());
        project.setTelephone(dto.getTelephone());
        project.getSubmitStatus().put(PROJECT_BASIS_MSG, true);
        return project;
    }
}
