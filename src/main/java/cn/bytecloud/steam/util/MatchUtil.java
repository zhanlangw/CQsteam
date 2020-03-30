package cn.bytecloud.steam.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 手机号，邮箱正则匹配
 */
public class MatchUtil {

    /**
     * 手机号码正则匹配
     *
     * @author zhanlang
     */
    public static boolean checkTelephone(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))" +
                "\\d{8}$";
        if (phone.length() != 11) {
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            return isMatch;
        }
    }

    /**
     * 邮箱正则匹配
     *
     * @author zhanlang
     */
    public static boolean checkEmail(String email) {
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        return Pattern.matches(regex, email);
    }


    /**
     * 检查密码是否包含数字和英文
     *
     * @param password
     * @return
     */
    public static boolean checkPassword(String password) {
        boolean numFlag = false;
        boolean englishFlag = false;
        for (int i = 0; i < password.length(); i++) {
            int num = (int) password.charAt(i);
            if (!numFlag) {
                if (num <= 57 && num >= 49) {
                    numFlag = true;
                }
            }
            if (!englishFlag) {
                if ((num <= 90 && num >= 65) || (num <= 122 && num >= 97)) {
                    englishFlag = true;
                }
            }
        }
        return (numFlag && englishFlag);
    }

    public static int DESC_CHINESE_LENGTH_MAX = 50;
    public static int DESC_CHAR_LENGTH_MAX = 100;
    public static int NAME_CHINESE_LENGTH_MAX = 10;
    public static int NAME_CHAR_LENGTH_MAX = 20;
    public static int PASSWORD_LENGTH_MAX = 8;
    public static int PASSWORD_LENGTH_MIN = 16;

    public static boolean checkChineseChar(char c) {
        return String.valueOf(c).matches("[\u4e00-\u9fa5]");
    }

    public static boolean checkPswd(String password) {
        if (password.contains(" ") || password.contains("   ")) {
            return false;
        }
        int length = password.length();
        if (length < PASSWORD_LENGTH_MAX || length > PASSWORD_LENGTH_MIN) {
            return false;
        }
        boolean numFlag = false;
        boolean englishFlag = false;
        for (int i = 0; i < password.length(); i++) {
            int num = (int) password.charAt(i);
            if (!numFlag) {
                if (num <= 57 && num >= 49) {
                    numFlag = true;
                }
            }
            if (!englishFlag) {
                if ((num <= 90 && num >= 65) || (num <= 122 && num >= 97)) {
                    englishFlag = true;
                }
            }
        }
        return (numFlag && englishFlag);
    }

    public static boolean checkDesc(String desc) {
        desc = desc.trim();
        if (desc.length() > DESC_CHAR_LENGTH_MAX) {
            return false;
        }
        int chineseCount = 0;
        for (int i = 0; i < desc.length(); i++) {
            if (checkChineseChar(desc.charAt(i))) {
                chineseCount++;
            }
        }
        if (chineseCount > DESC_CHINESE_LENGTH_MAX) {
            return false;
        }
        return true;
    }

    public static boolean checkName(String name) {
        name = name.trim();
        if (name.length() > NAME_CHAR_LENGTH_MAX) {
            return false;
        }
        int chineseCount = 0;
        for (int i = 0; i < name.length(); i++) {
            if (checkChineseChar(name.charAt(i))) {
                chineseCount++;
            }
        }
        if (chineseCount > NAME_CHINESE_LENGTH_MAX) {
            return false;
        }
        return true;
    }
}
