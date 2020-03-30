package cn.bytecloud.steam.util;

import cn.bytecloud.steam.area.entity.Area;
import cn.bytecloud.steam.area.service.AreaService;
import cn.bytecloud.steam.category.entity.Category;
import cn.bytecloud.steam.category.entity.StageType;
import cn.bytecloud.steam.project.entity.Project;
import cn.bytecloud.steam.school.service.SchoolService;

public class PathUtil {
    public static String getProjectPath() {
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        for (int i = 0; i < 4; i++) {
            int lastIndexOf = path.lastIndexOf("/");
            path = path.substring(0, lastIndexOf);
        }
        return path + "/fileserver";
    }

    public static String getFileName(String path) {
        int index = path.lastIndexOf("/");
        return index == -1 ? path : path.substring(index + 1);
    }

    public static String getProjectSavePath(Project project, Category category, Integer type) {
        AreaService areaService = SpringUtils.getBean(AreaService.class);

        String path = getProjectPath();
        Area area = areaService.findOne(project.getMembers().get(0).getAreaId());
        String district = area.getName();
        area = areaService.findOne(area.getParentId());
        String city = area.getName();
        area = areaService.findOne(area.getParentId());
        String province = area.getName();
        String stage = StageType.getType(type).getEnumValue();


        return path + "/" + category.getName() + "_" + category.getId() + "/" + province + "/" + city + "/" + district +
                "/" + project.getGroup().getEnumValue() + "/" + SpringUtils.getBean(SchoolService.class).findOne
                (project.getSchoolId()).getName() + "/" + project.getNumber() + "_" + project.getName() + "/" + stage;
    }
}
