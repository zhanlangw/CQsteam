package cn.bytecloud.steam.user.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class LoginDto {
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}

