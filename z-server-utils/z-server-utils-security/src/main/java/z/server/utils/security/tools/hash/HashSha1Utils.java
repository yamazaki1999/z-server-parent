package z.server.utils.security.tools.hash;

import z.server.utils.security.tools.BaseSecurity;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @description: hash
 * HMAC-SHA1是一种安全的基于加密 hash函数和共享密钥的消息认证协议，它可以有效地防止数据在传输的过程中被截取和篡改，维护了数据的完整性、可靠性和安全性
 * @author: zbn
 * @create: 2018-04-14 14:29
 **/
public class HashSha1Utils extends BaseSecurity {

    private static final String HMAC_SHA1 = "HmacSHA1";

    /**
     * @description: 加密(返回16进制码)
     * @author: zbn
     * @create: 15:29 2018/4/13
     **/
    public static String encryptHandle(byte[] target, int frequency, String type, String key) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        return toHexString(encrypt(target, frequency, type, key));
    }

    /**
     * @description: 加密
     * @author: zbn
     * @create: 15:00 2018/4/13
     * @param: target, 加密次数
     * @return:  byte[]
     **/
    public static byte[] encrypt (byte[] target, int frequency, String type ,String key) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        byte[] encoderBytes = target;
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(UTF8), type);
        Mac instance = Mac.getInstance(type);
        instance.init(secretKeySpec);
        while(0 < frequency){
            encoderBytes = instance.doFinal(encoderBytes);
            -- frequency;
            System.out.println(Arrays.toString(encoderBytes));
        }
        return encoderBytes;
    }
}
