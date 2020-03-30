package cn.bytecloud.steam.util;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

import java.util.Random;

@Slf4j
public class SmsUtil {
    public static void SendSms(String telephone, String code) {
        DefaultProfile profile = DefaultProfile.getProfile("default", "LTAITDFfKJIRvZRy",
                "ta2DgzKAvRTjNMHHy2IIqo06kOeS4L");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("PhoneNumbers", telephone);
        request.putQueryParameter("SignName", "青少年STEAM科创大赛");
        request.putQueryParameter("TemplateCode", "SMS_160260011");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        request.putQueryParameter("TemplateParam", jsonObject.toJSONString());
        try {
            CommonResponse response = client.getCommonResponse(request);
            String data = response.getData();
            if ("OK".equals(JSONObject.parseObject(data).getString("Message"))) {
                log.info("【验证码短信发送成功】： 电话——" + telephone + "   验证码——" + code);
            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    public static void sendProjectMsg(String telephone) {
        DefaultProfile profile = DefaultProfile.getProfile("default", "LTAITDFfKJIRvZRy",
                "ta2DgzKAvRTjNMHHy2IIqo06kOeS4L");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("PhoneNumbers", telephone);
        request.putQueryParameter("SignName", "青少年STEAM科创大赛");
        request.putQueryParameter("TemplateCode", "SMS_160160677");
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("code", code);
//        request.putQueryParameter("TemplateParam", jsonObject.toJSONString());
        try {
            CommonResponse response = client.getCommonResponse(request);
            if ("OK".equals(JSONObject.parseObject(response.getData()).getString("Message"))) {
                log.info("【项目提醒短信发送成功】： 电话——" + telephone);
            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        sendProjectMsg("15756378324");
    }
}


