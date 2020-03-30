package cn.bytecloud.steam;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class SteamApplication extends WebMvcConfigurerAdapter {
    public static void main(String[] args) {
        log.info("准备启动");
        SpringApplication.run(SteamApplication.class, args);
        log.info("启动成功！");
    }
}

