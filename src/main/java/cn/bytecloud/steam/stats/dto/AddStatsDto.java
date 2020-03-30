package cn.bytecloud.steam.stats.dto;

import cn.bytecloud.steam.stats.entity.Stats;
import cn.bytecloud.steam.util.StringUtil;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

import static cn.bytecloud.steam.constant.ModelConstant.STATS_BID;

@Data
public class AddStatsDto {
    private String username;

    @NotEmpty
    private String bid;

    public Stats toData() {
        Stats stats = new Stats();
        stats.setUsername(username);
        stats.setBid(bid);
        Date date = new Date(System.currentTimeMillis());
        String statsTime = StringUtil.getStatsTime(date);
        long time = StringUtil.getStatsTime(statsTime).getTime();
        stats.setTime(time);
        return stats;
    }

}
