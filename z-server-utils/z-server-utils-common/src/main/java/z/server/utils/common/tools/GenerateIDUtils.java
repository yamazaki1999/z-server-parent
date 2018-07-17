package z.server.utils.common.tools;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Stream;

/**
 * @description:
 * @author: zbn
 * @create: 2018-04-07 10:18
 **/
public class GenerateIDUtils {

    /**
     * 日期格式20180102130302332
     */
    public static final Integer DATE_FORMAT = 1;

    /**
     * 随机数
     */
    public static final Integer RANDOM_FORMAT = 2;

    /**
     * UUID 023354239fba4a4eaef7e75e7eb3711f
     */
    public static final Integer UUID_FORMAT = 3;

    /**
     * 时间LONG格式
     */
    public static final Integer DATE_LONG_FORMAT = 4;

    public static String generate(String prefix, String suffix, Integer format, Integer bit){

        String content;
        Integer length = bit;
        int pLength = null != prefix ? prefix.length() : 0;
        int sLength = null != suffix ? suffix.length() : 0;
        bit = null != bit ? bit - pLength - sLength : 32 - pLength - sLength;

        switch (format){
            case 2 :
                content = generateIDByRanFm(bit);
                break;
            case 3 :
                content = generateUUIDFm();
                break;
            case 4 :
                content = generateIDByDateLFm();
                break;
            default: content = generateIDByDateFm();
        }

        if(bit > content.length()){
            int rd = bit - content.length();
            content = content.concat(generateIDByRanFm(rd));
        }

        if(0 < pLength){
            content = prefix.concat(content);
        }

        if(0 < sLength){
            content = content.concat(suffix);
        }

        if(null != length){
            return content.substring(0, length);
        }
        return content;
    }

    /**
    * @description: 按时间格式yyyyMMddHHmmssSSS生成ID 17位
    * @author: zbn
    * @create: 16:43 2018/4/7
    **/
    public static String generateIDByDateFm(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(new Date());
    }

    /**
     * @description: 按时间格式Long生成ID 貌似13位
     * @author: zbn
     * @create: 16:43 2018/4/7
     **/
    public static String generateIDByDateLFm(){
        Long timeLong = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        return timeLong.toString();
    }

    /**
     * @description: UUID生成ID
     * @author: zbn
     * @create: 16:43 2018/4/7
     **/
    public static String generateUUIDFm(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * @description: 随机数生成ID
     * @author: zbn
     * @create: 16:43 2018/4/7
     **/
    public static String generateIDByRanFm(int bit){
        String rd = Stream.generate(Math::random).limit(1).findAny().orElse(null).toString();
        String s = rd.substring(rd.indexOf(".") + 1, rd.length());
        if(s.length() >= bit){
            return s.substring(0, bit);
        }
        return s.concat(generateIDByRanFm(bit  - s.length()));
    }
}
