package cn.bytecloud.steam.project.dto;

import cn.bytecloud.steam.category.entity.GroupType;
import cn.bytecloud.steam.project.entity.Member;
import cn.bytecloud.steam.project.entity.Project;
import cn.bytecloud.steam.project.entity.SubmitType;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class UpdBasisDto {
    @NotEmpty
    private String id;

    @NotNull
    private Integer count;

    @NotEmpty
    @Valid
    private List<AddBasisMemberDto> members = new ArrayList<>();

    public Project toData(Project project) {
        List<Member> members = project.getMembers();
        for (int i = 0; i < this.members.size(); i++) {
            AddBasisMemberDto memberDto = this.members.get(i);
            if (members.size() > i) {
                Member member = members.get(i);
                member.setName(memberDto.getName());
                member.setTelephone(memberDto.getTelephone());
                member.setIdCard(memberDto.getIdCard());
                member.setEmail(memberDto.getEmail());
                member.setAreaId(memberDto.getAreaId());
                member.setSchoolId(memberDto.getSchoolId());
                member.setGroup(GroupType.getType(memberDto.getGroup()));

            } else {
                members.add(memberDto.toData());
            }

        }

        boolean flag = this.members.size() < members.size();
        while (flag) {
            members.remove(members.size() - 1);
            flag = this.members.size() < members.size();
        }

        project.getSubmitStatus().put(SubmitType.PROJECT_BASIS_MSG, false);

        project.setCount(count);
        project.setSubmitFlag(false);
        return project;
    }
}
