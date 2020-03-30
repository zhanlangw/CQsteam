package cn.bytecloud.steam.project.dto;

import cn.bytecloud.steam.category.entity.GroupType;
import cn.bytecloud.steam.project.entity.Member;
import cn.bytecloud.steam.util.IDCardUtil;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
public class AddBasisMemberDto {
    @NotEmpty
    @Length(max = 20, min = 0)
    private String name;

    @NotEmpty
    private String telephone;

    @NotEmpty
    private String idCard;

    @NotEmpty
    private String email;

    @NotEmpty
    private String areaId;

    @NotEmpty
    private String schoolId;

    @NotNull
    private Integer group;

    public Member toData() {
        Member member = new Member();
        member.setName(name);
        member.setTelephone(telephone);

        member.setIdCard(idCard);
        member.setEmail(email);
        member.setAreaId(areaId);
        member.setSchoolId(schoolId);
        member.setGroup(GroupType.getType(group));

        member.setGender(IDCardUtil.getGenderByIdCard(idCard));
        member.setBirthday(IDCardUtil.getYearByIdCard(idCard) + "年" + IDCardUtil.getMonthByIdCard(idCard) + "月" +
                IDCardUtil.getDateByIdCard(idCard) + "日");
        return member;
    }
}
