package cn.bytecloud.steam.project.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class AddTeacherDto {
    @NotEmpty
    private String id;

    @NotNull
    @Valid
    private List<TeacherDto> teachers = new ArrayList<>();
}
