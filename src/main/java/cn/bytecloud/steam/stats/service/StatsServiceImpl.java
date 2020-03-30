package cn.bytecloud.steam.stats.service;

import cn.bytecloud.steam.project.entity.Project;
import cn.bytecloud.steam.project.service.ProjectService;
import cn.bytecloud.steam.stats.dao.StatsDao;
import cn.bytecloud.steam.stats.dao.StatsRepository;
import cn.bytecloud.steam.stats.dto.AddStatsDto;
import cn.bytecloud.steam.stats.dto.ExportDto1;
import cn.bytecloud.steam.stats.dto.ExportDto2;
import cn.bytecloud.steam.stats.dto.StatsDto;
import cn.bytecloud.steam.stats.entity.Stats;
import cn.bytecloud.steam.user.service.UserService;
import cn.bytecloud.steam.util.ExcelUtil;
import cn.bytecloud.steam.util.MD5Util;
import cn.bytecloud.steam.util.StringUtil;
import cn.bytecloud.steam.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class StatsServiceImpl implements StatsService {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private StatsDao dao;

    @Autowired
    private StatsRepository repository;

    public static void main(String[] args) {
        System.out.println(MD5Util.getMD5("root"));
    }

    @Override
    public Object signUpStats(StatsDto dto) {
        List<Project> list = projectService.findAll();
        Integer count = list.stream().map(Project::getMembers).map(List::size).reduce(0, (x, y) -> x + y);
        Map map = new HashMap();
        map.put("data", checkData(projectService.signUpStats(dto), dto));
        map.put("totalCount", count);
        return map;
    }

    @Override
    public void exportSignUp(ExportDto1 dto1, ExportDto2 dto2, HttpServletResponse response, HttpServletRequest request) throws IOException {
        LinkedList<HashMap> list1 = checkData(projectService.signUpStats(dto1.toDto()), dto1.toDto());
        LinkedList<HashMap> list2 = checkData(projectService.signUpStats(dto2.toDto()), dto2.toDto());
        export(response, list1, list2, "报名统计数据_", request);
    }

    @Override
    public void save(Stats stats) {
        repository.save(stats);
    }

    @Override
    public Object registerStats(StatsDto dto) {
        CompletableFuture<LinkedList<HashMap>> dataFuture = CompletableFuture.supplyAsync(() ->
                check(userService.registerStats(dto), dto));
        CompletableFuture<Integer> countFuture = CompletableFuture.supplyAsync(() -> userService.registerCount());
        Map map = new HashMap();
        map.put("totalCount", countFuture.join());
        map.put("data", dataFuture.join());
        return map;
    }

    @Override
    public void exportRegister(ExportDto1 dto1, ExportDto2 dto2, HttpServletResponse response, HttpServletRequest request) throws IOException {
        LinkedList<HashMap> list1 = check(userService.registerStats(dto1.toDto()), dto1.toDto());
        LinkedList<HashMap> list2 = check(userService.registerStats(dto2.toDto()), dto2.toDto());
        export(response, list1, list2, "注册统计数据_", request);

    }


    @Override
    public Object visitor(StatsDto dto) {
        Map map = new HashMap();
        List<String> data = new ArrayList<>();
        dao.visitor(dto).forEach(item -> data.add(StringUtil.getStatsTime(new Date(item.getTime()))));
        LinkedList<HashMap> list = check(data, dto);
        map.put("totalCount", repository.count());
        map.put("data", list);
        return map;
    }

    @Override
    public void exportVisitor(StatsDto dto, HttpServletResponse response, HttpServletRequest request) throws IOException {
        List<String> data = new ArrayList<>();
        dao.visitor(dto).forEach(item -> data.add(StringUtil.getStatsTime(new Date(item.getTime()))));
        LinkedList<HashMap> list = check(data, dto);
        export(response, list, "访客统计数据_", request);
    }

    @Override
    public Object wordStats(StatsDto dto) {
        Map map = new HashMap();
        LinkedList<HashMap> data = checkData(projectService.wordStats(dto), dto);
        long count = projectService.wordCount();
        map.put("totalCount", count);
        map.put("data", data);
        return map;
    }

    @Override
    public void exportWord(ExportDto1 dto1, ExportDto2 dto2, HttpServletResponse response, HttpServletRequest request) throws IOException {
        LinkedList<HashMap> list1 = checkData(projectService.wordStats(dto1.toDto()), dto1.toDto());
        LinkedList<HashMap> list2 = checkData(projectService.wordStats(dto2.toDto()), dto2.toDto());
        export(response, list1, list2, "提交word数据统计_", request);
    }

    @Override
    public Object submitStats(StatsDto dto) {
        Map map = new HashMap();
        LinkedList<HashMap> data = checkData(projectService.submitStats(dto), dto);
        Integer count = projectService.submitCount();
        map.put("totalCount", count);
        map.put("data", data);
        return map;
    }


    @Override
    public void exportSubmit(ExportDto1 dto1, ExportDto2 dto2, HttpServletResponse response, HttpServletRequest request) throws IOException {
        LinkedList<HashMap> list1 = checkData(projectService.submitStats(dto1.toDto()), dto1.toDto());
        LinkedList<HashMap> list2 = checkData(projectService.submitStats(dto2.toDto()), dto2.toDto());
        export(response, list1, list2, "最终确认文档数据统计_", request);
    }

    @Override
    public Object prizeStats(StatsDto dto) {
        return projectService.prizeStats(dto);
    }


    @Override
    public void exportPrize(ExportDto1 dto1, ExportDto2 dto2, HttpServletResponse response, HttpServletRequest request) throws IOException {
        Map[] data1 = projectService.prizeStats(dto1.toDto());
        Map[] data2 = projectService.prizeStats(dto1.toDto());
        Vector<Vector<String>> rowName = new Vector<>();
        for (int i = 0; i < data1.length; i++) {
            Vector<String> row = new Vector<>();
            Map item1 = data1[i];
            Map item2 = data2[i];
            row.add((String) item1.get("prize"));
            row.add(item1.get("count") + "");
            row.add(item2.get("count") + "");
            rowName.add(row);
        }

        //设置行名 Vector<String>
        Vector<String> rowTopName = new Vector<String>();
        rowTopName.add("获奖等级");
        rowTopName.add("数量");

        String fileName = "获奖等级统计_" + StringUtil.getExportTime();

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
    public synchronized void add(AddStatsDto dto) {
        Stats stats = dto.toData();
        if (repository.findByUsernameAndBidAndTime(stats.getUsername(), stats.getBid(), stats.getTime()).size() == 0) {
            stats.setId(UUIDUtil.getUUID());
            repository.save(stats);
        }
    }


    private LinkedList<HashMap> check(List<String> data, StatsDto dto) {
        data = new ArrayList(data);
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        Calendar.getInstance();
        start.setTime(new Date(dto.getStartTime()));
        end.setTime(new Date(dto.getEndTime()));
        LinkedList<HashMap> list = new LinkedList<>();
        while (start.getTime().getTime() <= end.getTime().getTime()) {
            String time = StringUtil.getStatsTime(start.getTime());
            HashMap item = new HashMap<>();
            item.put("count", 0);
            item.put("createTime", time);
            if (data.size() > 0) {
                while (time.equals(data.get(0))) {
                    item.put("count", (Integer) item.get("count") + 1);
                    data.remove(0);
                    if (data.size() == 0) {
                        break;
                    }
                }
            }
            list.add(item);
            start.add(Calendar.DAY_OF_MONTH, 1);
        }
        return list;
    }

    private LinkedList<HashMap> checkData(List<HashMap> data, StatsDto dto) {
        data = new ArrayList(data);
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        Calendar.getInstance();
        start.setTime(new Date(dto.getStartTime()));
        end.setTime(new Date(dto.getEndTime()));

        LinkedList<HashMap> list = new LinkedList<>();
        while (start.getTime().getTime() <= end.getTime().getTime()) {
            String time = StringUtil.getStatsTime(start.getTime());
            HashMap item = new HashMap<>();
            item.put("count", 0);
            item.put("createTime", time);
            if (data.size() > 0) {
                while (time.equals(StringUtil.getStatsTime(new Date((Long) (data.get(0).get("time")))))) {
                    item.put("count", (Integer) item.get("count") + (Integer) data.get(0).get("count"));
                    data.remove(0);
                    if (data.size() == 0) {
                        break;
                    }
                }
            }
            list.add(item);
            start.add(Calendar.DAY_OF_MONTH, 1);
        }
        return list;
    }

    public void export(HttpServletResponse response, LinkedList<HashMap> list1, LinkedList<HashMap> list2, String
            fileName, HttpServletRequest request) throws IOException {
        Vector<Vector<String>> rowName = new Vector<>();
        for (int i = 0; i < list1.size(); i++) {
            HashMap item1 = list1.get(i);
            HashMap item2 = list2.get(i);
            Vector<String> row = new Vector<>();
            row.add((String) item1.get("createTime"));
            row.add(item1.get("count") + "");
            row.add(item2.get("count") + "");
            rowName.add(row);
        }

        //设置行名 Vector<String>
        Vector<String> rowTopName = new Vector<String>();
        rowTopName.add("时间");
        rowTopName.add("数量");
        rowTopName.add("数量");


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

    private void export(HttpServletResponse response, LinkedList<HashMap> list, String fileName, HttpServletRequest request) throws IOException {
        Vector<Vector<String>> rowName = new Vector<>();
        for (int i = 0; i < list.size(); i++) {
            HashMap item1 = list.get(i);
            Vector<String> row = new Vector<>();
            row.add((String) item1.get("createTime"));
            row.add(item1.get("count") + "");
            rowName.add(row);
        }

        //设置行名 Vector<String>
        Vector<String> rowTopName = new Vector<String>();
        rowTopName.add("时间");
        rowTopName.add("数量");

        fileName = fileName + StringUtil.getExportTime();

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
}
