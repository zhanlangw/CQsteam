package cn.bytecloud.steam.stats.service;

import cn.bytecloud.steam.stats.dto.AddStatsDto;
import cn.bytecloud.steam.stats.dto.ExportDto1;
import cn.bytecloud.steam.stats.dto.ExportDto2;
import cn.bytecloud.steam.stats.dto.StatsDto;
import cn.bytecloud.steam.stats.entity.Stats;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface StatsService {
    Object signUpStats(StatsDto dto);

    void save(Stats stats);

    Object registerStats(StatsDto dto);

    Object wordStats(StatsDto dto);

    Object submitStats(StatsDto dto);

    Object prizeStats(StatsDto dto);

    void add(AddStatsDto dto);

    void exportRegister(ExportDto1 dto1, ExportDto2 dto2, HttpServletResponse response, HttpServletRequest request) throws IOException;

    Object visitor(StatsDto dto);

    void exportWord(ExportDto1 dto1, ExportDto2 dto2, HttpServletResponse response, HttpServletRequest request) throws IOException;

    void exportSubmit(ExportDto1 dto1, ExportDto2 dto2, HttpServletResponse response, HttpServletRequest request) throws IOException;

    void exportPrize(ExportDto1 dto1, ExportDto2 dto2, HttpServletResponse response, HttpServletRequest request) throws IOException;

    void exportVisitor(StatsDto dto, HttpServletResponse response, HttpServletRequest request) throws IOException;

    void exportSignUp(ExportDto1 dto1, ExportDto2 dto2, HttpServletResponse response, HttpServletRequest request) throws IOException;
}
