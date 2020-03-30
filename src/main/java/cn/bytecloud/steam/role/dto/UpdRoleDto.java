package cn.bytecloud.steam.role.dto;

import cn.bytecloud.steam.role.entity.Role;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class UpdRoleDto extends AddRoleDto {

    @NotEmpty
    private String id;

    @Override
    public Role toData() {
        Role role = super.toData();
        role.setId(id);
        return role;
    }
}
