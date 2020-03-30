package cn.bytecloud.steam.project.dto;

import cn.bytecloud.steam.project.entity.MemberType;
import cn.bytecloud.steam.project.entity.Parent;
import jdk.nashorn.internal.ir.IfNode;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class AddBasisProjectMemberDto {

    private Integer type;

    @NotEmpty
    private String imagepath;

    @NotEmpty
    private String grade;

    private Parent parent = new Parent();

    private String address;

    private String passport;

    public MemberType getType() {
        return type != null ? MemberType.getType(type) : null;
    }
}
