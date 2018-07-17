package z.server.workflow;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: zbn
 * @create: 2018-05-01 23:03
 **/
@Component
//@PropertySource(value = "classpath:application.yml", ignoreResourceNotFound = true)
public class Test {

    @Value("${key}")
    public String key;

    public String z()
    {
        System.out.println(key);
        return "ttttt";
    }
}
