package cn.bytecloud.steam.rule.dto;

import cn.bytecloud.steam.rule.entity.Rule;
import cn.bytecloud.steam.util.EmptyUtil;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;


@Data
public class RuleDto {

    @NotNull
    private Integer number;

    private String id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    @NotEmpty
    private String path;

    @NotEmpty
    private String imagePath;

    public Rule toData() {
        Rule rule = new Rule();
        if (EmptyUtil.isNotEmpty(this.id)) {
            rule.setId(this.id);
        }
        rule.setNumber(this.number);
        rule.setTitle(this.title);
        rule.setContent(this.content);
        rule.setPath(this.path);
        rule.setImagePath(this.imagePath);
        return rule;
    }
}
