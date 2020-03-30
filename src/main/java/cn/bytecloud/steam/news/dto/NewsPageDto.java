package cn.bytecloud.steam.news.dto;

import cn.bytecloud.steam.base.dto.BasePageDto;
import lombok.Data;

@Data
public class NewsPageDto extends BasePageDto {
    private String title;

    private String creator;
}
