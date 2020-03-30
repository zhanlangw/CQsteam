package cn.bytecloud.steam.user.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class ResetPasswordDto {
    @NotEmpty
    private String id;

    @NotEmpty
    @Length(min = 8, max = 16)
    private String password;
}
