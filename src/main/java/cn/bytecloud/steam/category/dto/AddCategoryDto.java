package cn.bytecloud.steam.category.dto;

import cn.bytecloud.steam.category.entity.Category;
import cn.bytecloud.steam.category.entity.GroupType;
import cn.bytecloud.steam.category.entity.ParameterSetting;
import cn.bytecloud.steam.category.entity.TelephoneType;
import cn.bytecloud.steam.util.StringUtil;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class AddCategoryDto {

    @NotEmpty
    private String name;

    @NotEmpty
    private String abbreviation;

    @NotNull
    @Min(1)
    private Integer maxMember;

    @NotNull
    @Min(0)
    private Integer minMember;

    @NotEmpty
    private Set<Integer> group;

    @NotEmpty
    private List<String> areaIds;

    @NotEmpty
    private List<Integer> paramSetting = new ArrayList<>();

    @NotNull
    private Integer telephoneType;

    @NotNull
    private String signUpTime;

    @Valid
    @NotEmpty
    private List<StageDto> stages = new ArrayList<>();

    public long getSignUpTime() {
        return Objects.requireNonNull(StringUtil.getTime(signUpTime)).getTime();
    }

    public Category toDate() {
        Category category = new Category();
        category.setName(this.name);
        category.setAbbreviation(abbreviation);
        category.setMaxMember(this.maxMember);
        category.setMinMember(this.minMember);

        Set<GroupType> set = group.stream().map(GroupType::getType).collect(Collectors.toSet());
        category.setGroup(set);
        category.setAreaIds(this.areaIds);

        Map<Integer, String> map = new HashMap<>();
        Arrays.stream(ParameterSetting.values()).collect(Collectors.toList()).forEach(item -> map.put(item
                .getEnumType(), item.name()));
        List<ParameterSetting> list = paramSetting.stream().map(map::get).map(ParameterSetting::valueOf).collect
                (Collectors.toList());

        category.setParamSetting(list);
        category.setTelephoneType(telephoneType == 1 ? TelephoneType.PARENT_TELEPHONE : TelephoneType.TELEPHONE);
        category.setSignUpTime(getSignUpTime());
        category.setStages(this.stages.stream().map(StageDto::toData).collect(Collectors.toList()));
        return category;
    }
}
