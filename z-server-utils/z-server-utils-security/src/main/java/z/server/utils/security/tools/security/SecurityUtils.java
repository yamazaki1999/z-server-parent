package z.server.utils.security.tools.security;

import z.server.utils.security.tools.BaseSecurity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @description: 加密,不可逆摘要算法
 * 常用的摘要算法有MD5,SHA1。摘要算法是一个不可逆过程，就是无论多大数据，经过算法运算后都是生成固定长度的数据,一般结果使用16进制进行显示。
 * MD5和SHA1的区别：MD5结果是128位摘要，SHa1是160位摘要。那么MD5的速度更快，而SHA1的强度更高。
 *
 * 下面统一使用MD5算法进行说明，SHA1类似。
 * 主要用途有：验证消息完整性，安全访问认证，数据签名。
 *
 * 消息完整性：由于每一份数据生成的MD5值不一样，因此发送数据时可以将数据和其MD5值一起发送，然后就可以用MD5验证数据是否丢失、修改。
 * 安全访问认证：这是使用了算法的不可逆性质，（就是无法从MD5值中恢复原数据）对账号登陆的密码进行MD5运算然后保存，这样可以保证除了用户之外，即使数据库管理人员都无法得知用户的密码。
 * 数字签名：这是结合非对称加密算法和CA证书的一种使用场景。
 * 一般破解方法：字典法，就是将常用密码生成MD5值字典，然后反向查找达到破解目的，因此建议使用强密码。
 * @author: zbn
 * @create: 2018-04-13 14:41
 **/
public class SecurityUtils extends BaseSecurity {

    //加密后是16位byte
    private static final String TYPE_MD5 = "md5";

    //加密后是29位
    private static final String TYPE_SHA = "sha";


    /**
    * @description: 加密(返回16进制码)
    * @author: zbn
    * @create: 15:29 2018/4/13
    **/
    public static String encryptHandle(byte[] target, int frequency, String type) throws NoSuchAlgorithmException {
        return toHexString(encrypt(target, frequency, type));
    }

    /**
    * @description: 加密
    * @author: zbn
    * @create: 15:00 2018/4/13
    * @param: target, 加密次数
    * @return:  byte[]
    **/
    public static byte[] encrypt (byte[] target, int frequency, String type) throws NoSuchAlgorithmException {
        byte[] encoderBytes = target;
        MessageDigest instance = MessageDigest.getInstance(type);
        while(0 < frequency){
            encoderBytes = instance.digest(encoderBytes);
            -- frequency;
        }
        return encoderBytes;
    }

}
