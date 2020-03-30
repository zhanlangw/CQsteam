package cn.bytecloud.steam.news.dto;

import cn.bytecloud.steam.news.entity.News;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class UpdNewsDto extends AddNewsDto {
    @NotEmpty
    private String id;

    @Override
    public News toData() {
        News news = super.toData();
        news.setId(id);
        return news;
    }
}
