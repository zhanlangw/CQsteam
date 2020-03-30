package cn.bytecloud.steam.area.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Province {

    private String id;

    private String name;

    private ArrayList<City> city = new ArrayList<City>();

}
