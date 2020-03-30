package cn.bytecloud.steam.project.entity;

import cn.bytecloud.steam.area.entity.Area;
import cn.bytecloud.steam.area.service.AreaService;
import cn.bytecloud.steam.base.entity.BaseEntity;
import cn.bytecloud.steam.category.entity.Category;
import cn.bytecloud.steam.category.entity.GroupType;
import cn.bytecloud.steam.category.entity.ParameterSetting;
import cn.bytecloud.steam.category.entity.StageType;
import cn.bytecloud.steam.category.service.CategoryService;
import cn.bytecloud.steam.school.service.SchoolService;
import cn.bytecloud.steam.util.SpringUtils;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.bytecloud.steam.constant.ModelConstant.*;
import static cn.bytecloud.steam.project.entity.SubmitType.BASIS_MSG;
import static cn.bytecloud.steam.project.entity.SubmitType.PROJECT_BASIS_MSG;

/**
 * 项目
 */
@Data
@Document(collection = T_PROJECT)
public class Project extends BaseEntity {

    public Project() {
        submitStatus = new HashMap<>();
        submitTime = new ArrayList<>();
        docSubmitTime = new ArrayList<>();
        submitStatus.put(BASIS_MSG, false);
        submitStatus.put(PROJECT_BASIS_MSG, false);
        docsubmitFlag = false;
        zipFlag = false;
        eliminateFlag = false;
    }

    //名称
    @Field(PROJECT_NAME)
    private String name;

    //编号
    @Field(PROJECT_NUMBER)
    private String number;

    //类别id
    @Field(PROJECT_CATEGORY_ID)
    private String categoryId;

    //成员数量
    @Field(PROJECT_COUNT)
    private Integer count;

    //描述
    @Field(PROJECT_desc)
    private String desc;

    //地址
    @Field(PROJECT_ADDRESS)
    private String address;

    //老师信息
    @Field(PROJECT_TEACHERS)
    private List<Teacher> teachers = new ArrayList<>();

    //电话
    @Field(PROJECT_TELEPHONE)
    private String telephone;

    //材料
    @Field(PROJECT_MATERIALS)
    private List<Material> materials = new ArrayList<>();

    //提交状态
    @Field(PROJECT_SUBMIT_STATUS)
    private Map<SubmitType, Boolean> submitStatus;

    //成员
    @Field(PROJECT_MEMBERS)
    private List<Member> members = new ArrayList<>();

    //获奖等级
    @Field(PROJECT_PRIZE)
    private String prize;

    //学校id
    @Field(PROJECT_SCHOOL_ID)
    private String schoolId;

    //组别
    @Field(PROJECT_GROUP)
    private GroupType group;

    //区域id
    @Field(PROJECT_AREA_ID)
    private String areaId;

    //最终提交时间
    @Field(PROJECT_SUBMIT_TIME)
    private List<Long> submitTime;

    //doc提价时间
    @Field(PROJECT_DOC_SUBMIT_TIME)
    private List<Long> docSubmitTime;

    //是否提交doc
    @Field(PROJECT_DOC_SUBMIT_FLAG)
    private boolean docsubmitFlag;

    //是否最终提交
    @Field(PROJECT_SUBMIT_FLAG)
    private boolean submitFlag;

    //阶段
    @Field(PROJECT_STAGE)
    private StageType stage;

    //报名时间
    @Field(PROJECT_SIGN_UP_TIME)
    private long signUpTime;

    @Field(PROJECT_ZIP_FLAG)
    private boolean zipFlag;

    //是否淘汰
    @Field(PROJECT_ELIMINATE_FLAG)
    private boolean eliminateFlag;

    public Map toTeacherDto() {
        CategoryService categoryService = SpringUtils.getBean(CategoryService.class);
        Category category = categoryService.findOne(categoryId);
        Map map = new HashMap();
        map.put("id", super.getId());
        map.put("number", number);

        List<Map> teachers = new ArrayList<>();
        this.teachers.forEach(item -> {
            Map teacher = new HashMap();
            teacher.put("number", item.getNumber());
            teacher.put("name", item.getName());
            teacher.put("gender", item.getGender());
            teacher.put("telephone", item.getTelephone());
            if (category.getParamSetting().contains(ParameterSetting.SUBJECT)) {
                teacher.put("subject", item.getSubject());
            }
            teachers.add(teacher);
        });
        map.put("teachers", teachers);
        return map;

    }

    public Map toMaterialDto(StageType stageType) {
        Map map = new HashMap();
        map.put("id", super.getId());
        map.put("number", number);
        map.put("plan", number + (name == null ? "" : name));
        map.put("stageType", stageType.getEnumType());

        materials.stream().filter(item -> item.getStageType() == stageType).findFirst().ifPresent(item -> {
            map.put("pptPath", item.getPptPath());
            map.put("docPath", item.getDocPath());
            map.put("videoPath", item.getVideoPath());
            map.put("imagePath", item.getImagePath());
        });

        return map;

    }

    public Map toBasisDto() {
        Map map = new HashMap();

        Map category = new HashMap();
        map.put("id", super.getId());

        CategoryService categoryService = SpringUtils.getBean(CategoryService.class);
        Category cg = categoryService.findOne(categoryId);
        category.put("id", cg.getId());
        category.put("name", cg.getName());
        map.put("category", category);

        map.put("count", count);
        map.put("submitFlag", eliminateFlag || submitFlag);

        map.put("members", members.stream().map(Member::toBasisDto).collect(Collectors.toList()));
        return map;
    }

    public Map toBasisProjectDto(Category category) {
        List<ParameterSetting> paramSetting = category.getParamSetting();
        AreaService areaService = SpringUtils.getBean(AreaService.class);
        SchoolService schoolService = SpringUtils.getBean(SchoolService.class);

        Map map = new HashMap();

        map.put("id", super.getId());
        map.put("number", number);
        if (paramSetting.contains(ParameterSetting.NAME_AND_DESC)) {
            map.put("name", name);
            map.put("desc", desc);
        }

        Map areas = new HashMap();
        Area area = areaService.findOne(members.get(0).getAreaId());
        areas.put("district", area.getName());
        area = areaService.findOne(area.getParentId());
        areas.put("city", area.getName());
        area = areaService.findOne(area.getParentId());
        areas.put("province", area.getName());
        map.put("areas", areas);

        map.put("school", schoolService.findOne(members.get(0).getSchoolId()).getName());
        map.put("address", address);
        map.put("telephone", telephone);

        map.put("members", members.stream().map(item -> item.toBasisProjectDto(paramSetting)).collect(Collectors
                .toList()));
        return map;
    }
}
