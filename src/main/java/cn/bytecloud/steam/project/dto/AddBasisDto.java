package cn.bytecloud.steam.project.dto;

import cn.bytecloud.steam.exception.ByteException;
import cn.bytecloud.steam.exception.ErrorCode;
import cn.bytecloud.steam.category.entity.*;
import cn.bytecloud.steam.project.entity.Project;
import cn.bytecloud.steam.project.entity.SubmitType;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.bytecloud.steam.project.entity.SubmitType.TEACHER_MSG;

@Data
public class AddBasisDto {
    @NotEmpty
    private String categoryId;

    @NotNull
    private Integer count;

    @NotEmpty
    @Valid
    private List<AddBasisMemberDto> members = new ArrayList<>();

    public Project toData(Category category) throws ByteException {
        Project project = new Project();
        project.setCategoryId(categoryId);
        project.setCount(count);
        project.setMembers(members.stream().map(AddBasisMemberDto::toData).collect(Collectors.toList()));
        AddBasisMemberDto dto = members.stream().findFirst().orElseThrow(() -> new ByteException(ErrorCode
                .FAILURE, "没有成员信息"));
        project.setSchoolId(dto.getSchoolId());
        project.setAreaId(dto.getAreaId());
        project.setGroup(GroupType.getType(dto.getGroup()));
        StageType type = category.getStages().get(0).getType();

        if (type == StageType.PRELIMINARY) {
            project.getSubmitStatus().put(SubmitType.PRELIMINARY, false);
            project.setStage(StageType.PRELIMINARY);
        } else if (type == StageType.REMATCH) {
            project.getSubmitStatus().put(SubmitType.REMATCH, false);
            project.setStage(StageType.REMATCH);
        } else {
            project.getSubmitStatus().put(SubmitType.FINALS, false);
            project.setStage(StageType.FINALS);
        }

        project.getSubmitStatus().put(SubmitType.BASIS_MSG, true);
        if (category.getParamSetting().contains(ParameterSetting.GUIDE_TEACHER)) {
            project.getSubmitStatus().put(TEACHER_MSG, false);
        }
        project.setSignUpTime(System.currentTimeMillis());
        return project;
    }
}
