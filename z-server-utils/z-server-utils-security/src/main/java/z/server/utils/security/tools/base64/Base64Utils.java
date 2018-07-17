package z.server.utils.security.tools.base64;

import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

/**
 * @description:
 * @author: zbn
 * @create: 2018-04-13 19:50
 **/
public class Base64Utils {

    /**
    * @description: 编码
    * @author: zbn
    * @create: 19:56 2018/4/13
    **/
    public static String encode(byte[] target){
        return Base64.encodeBase64String(target);
    }

    /**
    * @description: 解码
    * @author: zbn
    * @create: 19:56 2018/4/13
    **/
    public static byte[] decoder(String target) throws IOException {
        BASE64Decoder base64Decoder = new BASE64Decoder();
        return base64Decoder.decodeBuffer(target);
    }
}
