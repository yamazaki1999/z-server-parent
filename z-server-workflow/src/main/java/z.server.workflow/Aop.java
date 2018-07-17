package z.server.workflow;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @description:
 * @author: zbn
 * @create: 2018-05-01 23:02
 **/
@Component
@Aspect
public class Aop {



    public Aop() {
        System.out.println("aop.................");
    }

    //    @Around(value="execution(* com.qcc.beans.aop.*.*(..))")
    @Around(value="execution(* z.server.workflow.Test.*(..))")
    public Object aroundTest(ProceedingJoinPoint jp) throws Throwable {

          String methodName = jp.getSignature().getName();
          Object result = null;

          System.out.println("【环绕通知中的--->前置通知】：the method 【" + methodName + "】 begins with " + Arrays.asList(jp.getArgs()));
    //          //执行目标方法
          result = jp.proceed();
          System.out.println("【环绕通知中的--->返回通知】：the method 【" + methodName + "】 ends with result:" + result);
          return result;
    }
}
