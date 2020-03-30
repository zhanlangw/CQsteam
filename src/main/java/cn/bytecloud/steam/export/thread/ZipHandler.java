package cn.bytecloud.steam.export.thread;

import cn.bytecloud.steam.area.entity.Area;
import cn.bytecloud.steam.area.service.AreaService;
import cn.bytecloud.steam.category.entity.Category;
import cn.bytecloud.steam.category.entity.StageType;
import cn.bytecloud.steam.category.service.CategoryService;
import cn.bytecloud.steam.constant.SystemConstant;
import cn.bytecloud.steam.export.entity.Export;
import cn.bytecloud.steam.export.entity.ExportStatus;
import cn.bytecloud.steam.export.service.ExportService;
import cn.bytecloud.steam.project.entity.Material;
import cn.bytecloud.steam.project.entity.Project;
import cn.bytecloud.steam.project.service.ProjectService;
import cn.bytecloud.steam.school.service.SchoolService;
import cn.bytecloud.steam.util.FileUtil;
import cn.bytecloud.steam.util.IpUtil;
import cn.bytecloud.steam.util.UUIDUtil;
import cn.bytecloud.steam.util.ZipUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ZipHandler {
    @Autowired
    private SystemConstant systemConstant;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private ExportService exportService;

    private ZipThread zipThread;

    public ZipThread getInstance() {
        if (zipThread == null) {
            zipThread = new ZipThread();
        }
        return zipThread;
    }

    public class ZipThread implements Runnable {

        private ConcurrentHashMap<Export, Integer> map;

        public void addZipMsg(Export export, Integer type) {
            map.put(export, type);
        }

        public ZipThread() {
            map = new ConcurrentHashMap<>();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                map.forEach(this::accept);
            }
        }

        private void accept(Export export, Integer type) {
            try {
                File deleted = new File(FileUtil.getProjectPath() + "/智能导出");
                if (deleted.exists()) {
                    deleteFile(deleted);
                }
                deleted = new File(FileUtil.getProjectPath() + "/智能导出");
                if (!deleted.exists()) {
                    deleted.mkdirs();
                }
                for (Project project : projectService.findByDocsubmitFlag(true)) {
                    if (project.getMaterials().size() > 0) {
                        for (Material material : project.getMaterials()) {
                            //保存文件路径
                            String path = getPath(project, type, material.getStageType());
                            List<String> list = new ArrayList<>();
                            list.addAll(material.getDocPath());
                            list.addAll(material.getPptPath());
                            list.addAll(material.getVideoPath());
                            list.addAll(material.getImagePath());
                            for (String filePath : list) {
                                //原生文件
                                File file = new File(FileUtil.getdeletePath(filePath));

                                if (file.exists() && file.isFile()) {
                                    String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
                                    File zipFile = new File(path, fileName);

                                    if (zipFile.exists() && zipFile.isFile()) {
                                        zipFile.delete();
                                        zipFile = new File(path, fileName);
                                    }

                                    // 判断路径是否存在,如果不存在就创建文件路径
                                    if (!zipFile.getParentFile().exists()) {
                                        final boolean mkdirs = zipFile.getParentFile().mkdirs();
                                    }
                                    // 将上传文件保存到一个目标文件当中
                                    OutputStream out = new FileOutputStream(zipFile);
                                    InputStream in = new FileInputStream(file);

                                    byte[] bytes = new byte[1024];
                                    int num;
                                    while ((num = in.read(bytes)) != -1) {
                                        out.write(bytes, 0, num);
                                    }
                                    out.close();
                                    in.close();
                                }
                            }
                        }
                    }
                }

                String sourceFileName = FileUtil.getProjectPath() + "/智能导出";
                String zipFilePath = FileUtil.getProjectPath() + "/" + UUIDUtil.getUUID() + ".zip";
                ZipUtil.zip(zipFilePath, sourceFileName);

                Export data = export;
                map.remove(export);
                data.setPath(zipFilePath);
                data.setStatus(ExportStatus.SUCCESS);
                data.setDownloadUrl("http://" + IpUtil.getSerIp() + ":" + systemConstant.port +
                        "/api/export/download?id=" + data.getId());
                exportService.save(data);
            } catch (Exception e) {
                Export data = export;
                map.remove(export);
                data.setStatus(ExportStatus.FAILURE);
                exportService.save(data);
                e.printStackTrace();
            } finally {
//                File file = new File(FileUtil.getProjectPath() + "/智能导出");
//                deleteFile(file);
            }
        }
    }

    public String getPath(Project project, Integer type, StageType stageType) {
        Category category = categoryService.findOne(project.getCategoryId());

        Area area = areaService.findOne(project.getMembers().get(0).getAreaId());
        String district = area.getName();
        area = areaService.findOne(area.getParentId());
        String city = area.getName();
        area = areaService.findOne(area.getParentId());
        String province = area.getName();
        String stage = stageType.getEnumValue();

        String path = FileUtil.getProjectPath() + "/智能导出";

        if (type == 1) {
            path = path + "/赛项" + category.getName() + "/" + province + "/" + district + "/" +
                    project.getGroup().getEnumValue() + "/" + schoolService.findOne(project
                    .getSchoolId()).getName() +
                    "/项目标号_" + project.getNumber() + "/" + stage;
        } else {
            path = path + "/赛项" + category.getName() + "/" + province + "/" + project.getGroup()
                    .getEnumValue()
                    + "/项目标号_" + project.getNumber() + "/" + stage;
        }
        return path;
    }

    private void deleteFile(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
        }
        file.delete();
    }
}

