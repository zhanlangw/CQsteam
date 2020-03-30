package cn.bytecloud.steam.stats.dao;

import cn.bytecloud.steam.stats.dto.StatsDto;
import cn.bytecloud.steam.stats.entity.Stats;

import java.util.List;

public interface StatsDao {

    List<Stats> visitor(StatsDto dto);
}
