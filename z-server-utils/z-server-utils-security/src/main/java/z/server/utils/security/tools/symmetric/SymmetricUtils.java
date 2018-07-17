package z.server.utils.security.tools.symmetric;

import z.server.utils.security.tools.BaseSecurity;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @description: 对称加密
 * 对称加密算法只是为了区分非对称加密算法。其中鲜明的特点是对称加密是加密解密使用相同的密钥，而非对称加密加密和解密时使用的密钥不一样。对于大部分情况我们都使用对称加密，而对称加密的密钥交换时使用非对称加密，这有效保护密钥的安全。非对称加密加密和解密密钥不同，那么它的安全性是无疑最高的，但是它加密解密的速度很慢，不适合对大数据加密。而对称加密加密速度快，因此混合使用最好。
 * 常用的对称加密算法有：AES和DES.
 *
 * DES：比较老的算法，一共有三个参数入口（原文，密钥，加密模式）。而3DES只是DES的一种模式，是以DES为基础更安全的变形，对数据进行了三次加密，也是被指定为AES的过渡算法。
 * AES:高级加密标准，新一代标准，加密速度更快，安全性更高（不用说优先选择）
 * AES密钥长度可以选择128位【16字节】，192位【24字节】和256位【32字节】密钥（其他不行，因此别乱设密码哦）。
 * DES和AES类似，指定为DES就行 key位7字节。3DES指定为”DESede”,DES密钥长度是56位，3DES加长了密钥长度，可以为112位或168位，所以安全性提高，速度降低。工作模式和填充模式标准和AES一样
 * @author: zbn
 * @create: 2018-04-14 02:05
 **/
public class SymmetricUtils extends BaseSecurity {

    private static final String TYPE_AES = "AES";

    private static final String TYPE_DES = "DES";

    /**
     * @description: 加密(返回16进制码)
     * @author: zbn
     * @create: 15:29 2018/4/13
     **/
    public static String encryptHandle(byte[] target, int frequency, String type, String key) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, UnsupportedEncodingException {
        return toHexString(encrypt(target, frequency, type, key));
    }

    /**
     * @description: 解密处理
     * @author: zbn
     * @create: 15:29 2018/4/13
     **/
    public static String decryptHandle(String target, int frequency, String type, String key) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, UnsupportedEncodingException {
        return new String(decrypt(toDecimalBytes(target), frequency, type, key));
    }

    /**
     * @description: 加密
     * @author: zbn
     * @create: 15:00 2018/4/13
     * @param: target, 加密次数
     * @return:  byte[]
     **/
    public static byte[] encrypt (byte[] target, int frequency, String type, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException {
        byte[] encryptBytes = target;
        Cipher cipher = Cipher.getInstance(type);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(UTF8), type));
        while(0 < frequency){
            encryptBytes = cipher.doFinal(target);
            -- frequency;
        }
        return encryptBytes;
    }

    /**
     * @description: 解密
     * @author: zbn
     * @create: 15:00 2018/4/13
     * @param: target, 解密次数
     * @return:  byte[]
     **/
    public static byte[] decrypt (byte[] target, int frequency, String type, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException {
        byte[] ecryptBytes = target;
        Cipher cipher = Cipher.getInstance(type);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(UTF8), type));
        while(0 < frequency){
            ecryptBytes = cipher.doFinal(target);
            -- frequency;
        }
        return ecryptBytes;
    }
}
