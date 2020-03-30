package cn.bytecloud.steam.role.dto;

import cn.bytecloud.steam.role.entity.Role;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.HashSet;
import java.util.Set;

@Data
public class AddRoleDto {
    @NotEmpty
    @Length(max = 10)
    private String name;

    @NotEmpty
    private Set<String> permissions = new HashSet<>();

    public Role toData() {
        Role role = new Role();
        role.setName(name);
        role.setPermissions(permissions);
        return role;
    }
}
