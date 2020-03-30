package cn.bytecloud.steam.news.dto;

import cn.bytecloud.steam.news.entity.News;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class AddNewsDto {
    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

//    @NotEmpty
    private String path;

    public News toData() {
        News news = new News();
        news.setTitle(title);
        news.setContent(content);
        news.setPath(path);
        return news;
    }
}
