package cn.bytecloud.steam.actor.actor;

import akka.actor.UntypedActor;
import cn.bytecloud.steam.actor.message.CaptchaMsg;
import cn.bytecloud.steam.util.SmsUtil;

public class Appactor extends UntypedActor {
    @Override
    public void onReceive(Object object) throws Exception {
        if (object instanceof CaptchaMsg) {
            CaptchaMsg captchaMsg = (CaptchaMsg) object;
//            SmsUtil.sendSms(captchaMsg.getTelephone(), captchaMsg.getCode());
        }
    }
}
