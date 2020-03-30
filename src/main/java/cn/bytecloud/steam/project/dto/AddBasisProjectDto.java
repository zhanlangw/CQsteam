package cn.bytecloud.steam.project.dto;

import lombok.Data;
import org.apache.commons.collections.ArrayStack;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.util.List;

@Data
public class AddBasisProjectDto {
    @NotEmpty
    private String id;

    private String name;

    private String desc;

    @NotEmpty
    @Length(max = 50)
    private String address;

    @NotEmpty
    private String telephone;

    @NotEmpty
    @Valid
    private List<AddBasisProjectMemberDto> members = new ArrayStack();
}
