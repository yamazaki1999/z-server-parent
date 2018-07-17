package z.server.utils.security.tools;

import javax.crypto.spec.SecretKeySpec;

/**
 * @description:
 * @author: zbn
 * @create: 2018-04-14 02:14
 **/
public class BaseSecurity {

    private static final String FORMAT = "%02X";

    public static final String UTF8 = "utf-8";

    /**
     * @description: 转16进制
     * @author: zbn
     * @create: 15:22 2018/4/13
     **/
    public static String toHexString(byte[] target){
        StringBuilder sb = new StringBuilder();
        for(byte b:target){
            sb.append(String.format(FORMAT, b & 0xFF));
        }
        return sb.toString();
    }

    /**
     * @description: 转10进制
     * @author: zbn
     * @create: 15:22 2018/4/13
     **/
    public static byte[] toDecimalBytes(String target){
        byte[] decimal = new byte[target.length()/2];
        int v = 0;
        for (int i = 0;i < target.length(); i = i + 2){
            decimal[v++] = (byte)Integer.parseInt(target.substring(i, i + 2), 16);
        }
        return decimal;
    }

}
