package cn.bytecloud.steam.user.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class UpdPasswordDto {

    @NotEmpty
//    @Length(max = 16, min = 8)
    private String oldPassword;

    @NotEmpty
    @Length(max = 16, min = 8)
    private String newPassword;

}
