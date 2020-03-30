package cn.bytecloud.steam.user.thread;

import cn.bytecloud.steam.util.EmptyUtil;
import cn.bytecloud.steam.util.SmsUtil;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class SMSHandler {
    private SMSThread smsThread;

    public SMSThread getInstance() {
        if (smsThread == null) {
            smsThread = new SMSThread();
        }
        return smsThread;
    }

    public class SMSThread implements Runnable {

        private ConcurrentHashMap<String, String> map;

        public void addCaptchaMsg(String telephone, String code) {
            if (EmptyUtil.isNotEmpty(telephone)) {
                map.put(telephone, EmptyUtil.isNotEmpty(code) ? code : "");
            }
        }

        public SMSThread() {
            map = new ConcurrentHashMap<>();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                map.forEach((key, value) -> {
                    if (EmptyUtil.isEmpty(value)) {
                        SmsUtil.sendProjectMsg(key);
                    } else {
                        SmsUtil.SendSms(key, value);
                    }
                    map.remove(key);
                });
            }
        }
    }
}
