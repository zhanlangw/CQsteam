package cn.bytecloud.steam.competitionMsg.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class CompetitionDto {
    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    private String path;
}
