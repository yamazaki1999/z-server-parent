package z.server.utils.security.tools.rsa;

import z.server.utils.security.tools.BaseSecurity;
import z.server.utils.security.tools.base64.Base64Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @description: 非对称加密
 * 1024位的证书，加密时最大支持117个字节，解密时为128；
 * 2048位的证书，加密时最大支持245个字节，解密时为256。
 * @author: zbn
 * @create: 2018-04-14 14:56
 **/
public class RSAUtils extends BaseSecurity {

    public static final String RSA_TYPE = "RSA";

    public static final int INIT_SIZE = 1024;

    public static final int INIT_SIZE2 = 2048;

    private static final int MAX_ENCRYPT_BLOCK = 117;

    private static final int MAX_DECRYPT_BLOCK = 128;

    private static final int MAX_ENCRYPT_BLOCK2 = 245;

    private static final int MAX_DECRYPT_BLOCK2 = 256;

    private static final String PUBLIC_KEY = "publicKey";

    private static final String PRIVATE_KEY = "privateKey";

    //签名算法
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    public static void main(String[] args) throws Exception {

//        for(int i = 0; i < 1000 ; i++){
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    Map<String, String> stringStringMap = null;
//                    try {
//                        stringStringMap = generateKeyPair(RSA_TYPE, INIT_SIZE2);
//                        String str="122222222222222233333333333333333231222222222222222333333333333333332312222222222222223333333333333333323122222222222222233333333333333333231222222222222222333333333333333332312222222222222223333333333333333323";
//                        String s = encryptHandle(str, RSA_TYPE, getPublicKey(stringStringMap.get(PUBLIC_KEY), RSA_TYPE), MAX_ENCRYPT_BLOCK2);
//                        System.out.println(s);
//                        String s1 = decryptHandle(s, RSA_TYPE, getPrivateKey(stringStringMap.get(PRIVATE_KEY), RSA_TYPE), MAX_DECRYPT_BLOCK2);
//                        System.out.println(s1);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }).start();
//        }

//        String ss = "test";
//        String sign = sign(ss.getBytes(), stringStringMap.get(PRIVATE_KEY));
//        System.out.println("sign : "+ sign);
//        System.out.println(verify(ss.getBytes(), stringStringMap.get(PUBLIC_KEY), sign));
    }

    /**
     * @description: 生成公私钥
     * @author: zbn
     * @create: 18:28 2018/4/14
     **/
    public static Map<String, String> generateKeyPair(String type, int initSize) throws NoSuchAlgorithmException {
        LinkedHashMap<String, String> keyPairMap = new LinkedHashMap<>();
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(type);
        kpg.initialize(initSize );
        KeyPair keyPair = kpg.generateKeyPair();
        keyPairMap.put(PUBLIC_KEY, Base64Utils.encode(keyPair.getPublic().getEncoded()));
        keyPairMap.put(PRIVATE_KEY, Base64Utils.encode(keyPair.getPrivate().getEncoded()));
        return keyPairMap;
    }

    /**
     * @description: 加密
     * @author: zbn
     * @create: 20:22 2018/4/14
     **/
    public static byte[] encrypt(byte[] target, String type, PublicKey publicKey, int eacheSize) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(type);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return segmentalProcessing(target, cipher, eacheSize);
    }

    /**
     * @description: 解密
     * @author: zbn
     * @create: 20:22 2018/4/14
     **/
    public static byte[] decrypt(byte[] target, String type, PrivateKey privateKey, int eacheSize) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(type);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return segmentalProcessing(target, cipher, eacheSize);
    }

    /**
     * @description: 加密处理
     * @author: zbn
     * @create: 20:22 2018/4/14
     **/
    public static String encryptHandle(String target, String type, PublicKey publicKey, int eacheSize) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return Base64Utils.encode(encrypt(target.getBytes(), type, publicKey, eacheSize));
    }

    /**
     * @description: 解密处理
     * @author: zbn
     * @create: 20:22 2018/4/14
     **/
    public static String decryptHandle(String target, String type, PrivateKey privateKey, int eacheSize) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        return new String(decrypt(Base64Utils.decoder(target), type, privateKey, eacheSize));
    }

    /**
     * @description: 加密/解密分段处理，加解密每次不能超117,解密128
     * @author: zbn
     * @create: 22:41 2018/4/14
     **/
    private static byte[] segmentalProcessing(byte[] target, Cipher cipher, int eacheSize) throws IllegalBlockSizeException, BadPaddingException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int start = 0;
        while (target.length - start > 0) {
            byte[] cache;
            if (target.length - start > eacheSize) {
                cache = cipher.doFinal(target, start, eacheSize);
            } else {
                cache = cipher.doFinal(target, start, target.length - start);
            }
            start = start + eacheSize;
            baos.write(cache, 0, cache.length);
        }
        return baos.toByteArray();
    }


    /**
     * @description: 公钥字符转PublicKey
     * @author: zbn
     * @create: 20:25 2018/4/14
     **/
    public static PublicKey getPublicKey(String publicKey, String type) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64Utils.decoder(publicKey));
        KeyFactory keyFactory = KeyFactory.getInstance(type);
        return keyFactory.generatePublic(x509EncodedKeySpec);
    }

    /**
     * @description: 私钥字符转PrivateKey
     * @author: zbn
     * @create: 20:25 2018/4/14
     **/
    public static PrivateKey getPrivateKey(String privateKey, String type) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64Utils.decoder(privateKey));
        KeyFactory keyFactory = KeyFactory.getInstance(type);
        return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
    }

    /**
    * @description: 生成签名
    * @author: zbn
    * @create: 0:01 2018/4/16
    **/
    public static String sign(byte[] data, String privateKey) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(getPrivateKey(privateKey, RSA_TYPE));
        signature.update(data);
        return Base64Utils.encode (signature.sign());
    }

    /**
    * @description: 校验签名
    * @author: zbn
    * @create: 0:08 2018/4/16
    **/
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(getPublicKey(publicKey, RSA_TYPE));
        signature.update(data);
        return signature.verify(Base64Utils.decoder(sign));
    }

}