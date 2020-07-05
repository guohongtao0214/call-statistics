package org.kaws.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.kaws.annotation.CallStatistics;
import org.kaws.annotation.StorageType;
import org.kaws.entity.MongoCallRecord;
import org.kaws.entity.MongoCallSuccessRecord;
import org.kaws.entity.MySQLCallRecord;
import org.kaws.entity.MySQLCallSuccessRecord;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;


/**
 * @Author: Heiky
 * @Date: 2020/6/8 9:47
 * @Description:
 * @Modified By:
 */

@Aspect
@Component
@Slf4j
public class CallAspect implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Lock lock;

    @Pointcut("@annotation(org.kaws.annotation.CallStatistics)")
    public void operationTarget() {
    }

    @Around("operationTarget()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法上注解的值
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CallStatistics annotation = signature.getMethod().getAnnotation(CallStatistics.class);
        StorageType storageType = annotation.value();

        Object res = null;
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String url = request.getRequestURI();
            String appid = request.getHeader("appID");
            String method = request.getMethod();
            String params = null;
            if ("GET".equals(method)) {
                Map<String, String[]> parameterMap = request.getParameterMap();
                params = objectMapper.writeValueAsString(parameterMap);
            } else {
                int len = request.getContentLength();
                ServletInputStream inputStream = request.getInputStream();
                byte[] buffer = new byte[len];
                inputStream.read(buffer, 0, len);
                params = new String(buffer);
            }
            url = method + " " + url;
            // 将用户标识、请求方式 请求地址、请求时间存入调用记录的集合
            lock.lock();
            if (StorageType.MYSQL.equals(storageType)) {
                List<MySQLCallRecord> mySQLCallRecords =
                        (List<MySQLCallRecord>) applicationContext.getBean("mySQLCallRecords");
                mySQLCallRecords.add(new MySQLCallRecord(appid, url, LocalDateTime.now(ZoneId.of("Asia/Shanghai"))));
            } else if (StorageType.MONGO.equals(storageType)) {
                List<MongoCallRecord> mongoCallRecords =
                        (List<MongoCallRecord>) applicationContext.getBean("mongoCallRecords");
                mongoCallRecords.add(new MongoCallRecord(appid, url, LocalDateTime.now(ZoneId.of("Asia/Shanghai"))));
            }
            lock.unlock();
            // 调用真正的接口
            res = joinPoint.proceed();
            // 将用户标识、请求方式 请求地址、请求参数、请求时间存入调用成功记录的集合
            lock.lock();
            if (StorageType.MYSQL.equals(storageType)) {
                List<MySQLCallSuccessRecord> mySQLCallSuccessRecords =
                        (List<MySQLCallSuccessRecord>) applicationContext.getBean("mySQLCallSuccessRecords");
                mySQLCallSuccessRecords.add(new MySQLCallSuccessRecord(appid, url, params, LocalDateTime.now(ZoneId.of("Asia/Shanghai"))));
            } else if (StorageType.MONGO.equals(storageType)) {
                List<MongoCallSuccessRecord> mongoCallSuccessRecords =
                        (List<MongoCallSuccessRecord>) applicationContext.getBean("mongoCallSuccessRecords");
                mongoCallSuccessRecords.add(new MongoCallSuccessRecord(appid, url, params, LocalDateTime.now(ZoneId.of("Asia/Shanghai"))));
            }
            lock.unlock();
            return res;
        } catch (Exception e) {
            log.error("统计方法执行异常", e);
        }
        return res;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
