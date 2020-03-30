package cn.bytecloud.steam.category.entity;

import cn.bytecloud.steam.area.entity.Area;
import cn.bytecloud.steam.area.service.AreaService;
import cn.bytecloud.steam.base.entity.BaseEntity;
import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.exception.ErrorCode;
import cn.bytecloud.steam.user.entity.User;
import cn.bytecloud.steam.user.service.UserService;
import cn.bytecloud.steam.util.SpringUtils;
import cn.bytecloud.steam.util.StringUtil;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.*;
import java.util.stream.Collectors;

import static cn.bytecloud.steam.constant.ModelConstant.*;

/**
 * 类别
 */
@Data
@Document(collection = T_CATEGORY)
public class Category extends BaseEntity {
    //名称
    @Field(CATEGORY_NAME)
    private String name;

    //简称
    @Field(CATEGORY_ABBREVIATION)
    private String abbreviation;

    //报名时间
    @Field(CATEGORY_SIGN_UP_TIME)
    private long signUpTime;

//    //报名结束时间
//    @Field(CATEGORY_SIGN_UP_END_TIME)
//    private long endTime;

    //状态
    @Field(CATEGORY_STATUS)
    private CategoryStatus status;

    //最大成员数
    @Field(CATEGORY_MAX_MEMBER)
    private Integer maxMember;

    //最小成员数
    @Field(CATEGORY_MIN_MEMBER)
    private Integer minMember;

    //组别
    @Field(CATEGORY_GROUP)
    private Set<GroupType> group;


    //组别
    @Field(CATEGORY_AREA_IDS)
    private List<String> areaIds;

    //阶段
    @Field(CATEGORY_SATGES)
    private List<Stage> stages = new ArrayList<>();

    //参数配置
    @Field(CATEGORY_PARAM_SETTING)
    private List<ParameterSetting> paramSetting = new ArrayList<>();

    //手机号类型
    @Field(CATEGORY_TELEPHONE_TYPE)
    private TelephoneType telephoneType;

    public Map<String, Object> toDto() {
        Map<String, Object> map = new HashMap();
        map.put("id", super.getId());
        map.put("name", name);
        map.put("abbreviation", abbreviation);
        map.put("signUpTime", StringUtil.getTime(new Date(signUpTime)));
        map.put("updateFlag", signUpTime > System.currentTimeMillis());
        map.put("status", getStatus(this));
        map.put("maxMember", maxMember);
        map.put("minMember", minMember);
        map.put("group", group.stream().map(GroupType::getEnumType).collect(Collectors.toSet()));

        AreaService areaService = SpringUtils.getBean(AreaService.class);
        List areas = new ArrayList();
        areaIds.forEach(id -> {
            Area area = areaService.findOne(id);
            if ("*".equals(id)) {
                areaService.findByParentId(null).forEach(one -> {
                    Map<String, String> item = new HashMap<String, String>();
                    item.put("id", one.getId());
                    item.put("name", one.getName());
                    areas.add(item);
                });
            } else {
                Map<String, String> item = new HashMap<String, String>();
                item.put("id", id);
                item.put("name", area.getName());
                areas.add(item);
            }

        });
        map.put("area", areas);

        List<Object> list = stages.stream()
                .map(item -> JSON.parseObject(JSON.toJSONString(item)))
                .peek(item -> item.put("endTime", StringUtil.getTime(new Date((long) item.remove("endTime")))))
                .peek(item -> item.put("type", StageType.valueOf((String) item.remove("type")).getEnumType()))
                .collect(Collectors.toList());
        map.put("stages", list);
        map.put("paramSetting", paramSetting.stream().map(ParameterSetting::getEnumType).collect(Collectors.toList()));
        map.put("telephoneType", telephoneType.getEnumType());
        map.put("createTime", StringUtil.getTime(new Date(super.getCreateTime())));
        map.put("updateTime", StringUtil.getTime(new Date(super.getUpdateTime())));

        UserService userService = SpringUtils.getBean(UserService.class);
        User user = userService.findOne(super.getCreatorId());

        map.put("creator", user.getName());
        return map;
    }

    public static Integer getStatus(Category category) {
        List<Stage> stages = category.getStages();
        long time = System.currentTimeMillis();
        if (category.getSignUpTime() > time) {
            return CategoryStatus.NOT_START.getEnumType();
        } else {
            stages = stages.stream().sorted(Comparator.comparing(Stage::getType)).collect(Collectors.toList());
            for (int i = 0; i < stages.size(); i++) {
                if (time < stages.get(i).getEndTime()) {
                    return CategoryStatus.valueOf(stages.get(i).getType().name()).getEnumType();
                }
            }
            return CategoryStatus.END.getEnumType();
        }
    }

    public static Integer getBeforeStatus(Category category) throws ByteException {
//        int index = -1;
        StageType type = null;
        List<Stage> stages = category.getStages();
        long time = System.currentTimeMillis();
        if (category.getSignUpTime() > time) {
            throw new ByteException(ErrorCode.FAILURE, "报名未开始");
        } else {
            int status = getStatus(category);
            stages = stages.stream().sorted(Comparator.comparing(Stage::getType)).collect(Collectors.toList());

            if (status == CategoryStatus.END.getEnumType()) {
                return stages.get(stages.size() - 1).getType().getEnumType();
            }
            for (int i = 0; i < stages.size(); i++) {
                if (stages.get(i).getType().getEnumType() == status - 1) {
                    break;
                } else {
                    type = stages.get(i).getType();
                }
            }
            if (type == null) {
                throw new ByteException(ErrorCode.FAILURE, stages.get(0).getType().getEnumValue() + "未结束");

            }
            return type.getEnumType();
        }
    }

}
