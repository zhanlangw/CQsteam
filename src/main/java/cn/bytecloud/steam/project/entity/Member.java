package cn.bytecloud.steam.project.entity;

import cn.bytecloud.steam.area.entity.Area;
import cn.bytecloud.steam.area.service.AreaService;
import cn.bytecloud.steam.category.entity.GroupType;
import cn.bytecloud.steam.category.entity.ParameterSetting;
import cn.bytecloud.steam.school.entity.School;
import cn.bytecloud.steam.school.service.SchoolService;
import cn.bytecloud.steam.util.SpringUtils;
import lombok.Data;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.bytecloud.steam.category.entity.ParameterSetting.MEMBER_LDENTITY;
import static cn.bytecloud.steam.category.entity.ParameterSetting.OTHER;
import static cn.bytecloud.steam.constant.ModelConstant.*;

@Data
public class Member {

    //名称
    @Field(PROJECT_MEMBER_NAME)
    private String name;

    //类型
    @Field(PROJECT_MEMBER_TYPE)
    private MemberType type;

    //头像图片地址
    @Field(PROJECT_MEMBER_IMAGE_PATH)
    private String imagePath;

    //性别
    @Field(PROJECT_MEMBER_GENDER)
    private String gender;

    //生日
    @Field(PROJECT_MEMBER_BIRTHDAY)
    private String birthday;

    //身份证
    @Field(PROJECT_MEMBER_ID_CARD)
    private String idCard;

    //护照
    @Field(PROJECT_MEMBER_PASSPORT)
    private String passport;

    //学校id
    @Field(PROJECT_MEMBER_SCHOOL_ID)
    private String schoolId;

    //组别
    @Field(PROJECT_MEMBER_GROUP)
    private GroupType group;

    //年级
    @Field(PROJECT_MEMBER_GRADE)
    private String grade;

    //电话
    @Field(PROJECT_MEMBER_TELEPHONE)
    private String telephone;

    //邮箱
    @Field(PROJECT_MEMBER_EMAIL)
    private String email;

    //父母信息
    @Field(PROJECT_MEMBER_PARENT)
    private Parent parent = new Parent();

    //区域id
    @Field(PROJECT_MEMBER_AREA_ID)
    private String areaId;

    //地址
    @Field(PROJECT_MEMBER_ADDRESS)
    private String address;

    public Map toBasisProjectDto(List<ParameterSetting> paramSetting) {
        AreaService areaService = SpringUtils.getBean(AreaService.class);
        SchoolService schoolService = SpringUtils.getBean(SchoolService.class);
        Map map = new HashMap();
        map.put("name", name);
        map.put("iamgePath", imagePath);
        map.put("gender", gender);
        map.put("birthday", birthday);
        map.put("idCard", idCard);
        map.put("group", group.getEnumType());

        Map areas = new HashMap();
        Area area = areaService.findOne(areaId);
        areas.put("district", area.getName());
        area = areaService.findOne(area.getParentId());
        areas.put("city", area.getName());
        area = areaService.findOne(area.getParentId());
        areas.put("province", area.getName());
        map.put("areas", areas);

        map.put("school", schoolService.findOne(schoolId).getName());
        map.put("grade", grade);

        if (paramSetting.contains(OTHER)) {
            map.put("parent", parent);
            map.put("passport", passport);
            map.put("address", address);
        }
        if (paramSetting.contains(MEMBER_LDENTITY)) {
            map.put("type", type == null ? null : type.getEnumType());
        }

        return map;
    }

    public Map toBasisDto() {
        Map map = new HashMap();
        map.put("name", name);
        map.put("telephone", telephone);
        map.put("idCard", idCard);
        map.put("email", email);

        Map areas = new HashMap();
        AreaService areaService = SpringUtils.getBean(AreaService.class);

        Area area = areaService.findOne(areaId);
        Map district = new HashMap();
        district.put("id", area.getId());
        district.put("name", area.getName());

        area = areaService.findOne(area.getParentId());
        Map city = new HashMap();
        List<Map> cityTree = new ArrayList<>();
        Area cityArea = area;
        areaService.findByParentId(area.getId()).forEach(item -> {
            Map itemData = new HashMap();
            itemData.put("key", item.getId());
            itemData.put("title", item.getName());
            itemData.put("uid", cityArea.getId());
            itemData.put("isLeaf", false);
            cityTree.add(itemData);
        });
        System.out.println(cityTree.size());
        city.put("tree", cityTree);
        city.put("id", area.getId());
        city.put("name", area.getName());

        area = areaService.findOne(area.getParentId());
        Map province = new HashMap();
        List<Map> provinceTree = new ArrayList<>();
        Area provinceArea = area;
        areaService.findByParentId(area.getId()).forEach(item -> {
            Map itemData = new HashMap();
            itemData.put("key", item.getId());
            itemData.put("title", item.getName());
            itemData.put("uid", provinceArea.getId());
            itemData.put("isLeaf", true);
            provinceTree.add(itemData);
        });
        province.put("tree", provinceTree);
        province.put("id", area.getId());
        province.put("name", area.getName());

        areas.put("district", district);
        areas.put("city", city);
        areas.put("province", province);
        map.put("areas", areas);

        SchoolService schoolService = SpringUtils.getBean(SchoolService.class);
        School school = schoolService.findOne(schoolId);
        Map schoolMap = new HashMap();
        schoolMap.put("id", school.getId());
        schoolMap.put("name", school.getName());
        map.put("school", schoolMap);

        map.put("group", group.getEnumType());

        return map;
    }
}
