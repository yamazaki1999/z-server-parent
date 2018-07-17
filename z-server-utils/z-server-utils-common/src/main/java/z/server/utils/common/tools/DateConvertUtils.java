package z.server.utils.common.tools;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * @description:
 * @author: zbn
 * @create: 2018-04-07 01:17
 **/
public class DateConvertUtils {

    public static void main(String[] args) {
        System.out.println(dateConvert(new Date()));
        System.out.println(localDateTimeConvert(LocalDateTime.now()));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(dateConvertByFormat(dateTimeFormatter, new Date()));
        System.out.println(strConvertByFormat(dateTimeFormatter, "2018-09-20 23:23:23"));
        System.out.println(LocalDate.now());
    }

    /**
    * @description: date转LocalDateTime
    * @author: zbn
    * @create: 9:39 2018/4/7
    **/
    public static LocalDateTime dateConvert(Date date){
        Instant instant = date.toInstant();
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
    * @description: LocalDateTime转date
    * @author: zbn
    * @create: 9:40 2018/4/7
    **/
    public static Date localDateTimeConvert(LocalDateTime localDateTime){
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
    * @description: 根据日期格式转换
    * @author: zbn
    * @create: 9:53 2018/4/7
    **/
    public static Date strConvertByFormat(DateTimeFormatter dateTimeFormatter, String date){
        TemporalAccessor ta = dateTimeFormatter.parse(date);
        Instant instant = LocalDateTime.from(ta).atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    /**
     * @description: 根据日期格式转换
     * @author: zbn
     * @create: 9:53 2018/4/7
     **/
    public static String dateConvertByFormat(DateTimeFormatter dateTimeFormatter, Date date){
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return localDateTime.format(dateTimeFormatter);
    }
}
