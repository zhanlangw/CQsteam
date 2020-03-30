package cn.bytecloud.steam.actor.message;

import lombok.Data;

@Data
public class CaptchaMsg {
    private String telephone;
    private String code;

    public CaptchaMsg(String telephone, String code) {
        this.telephone = telephone;
        this.code = code;
    }
}
