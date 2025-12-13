package com.sky.aspect;

// 自定义切面类 实现 公共字段自动填充

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

//声明是一个切面类
@Aspect
//这个表示 需要交给Spring容器进行管理
@Component
@Slf4j
public class AutoFillAspect {
//    切入点
//    表示拦截到加入了annotation 为止
    @Pointcut("execution(* com.sky.mapper..*.*(..)) && @annotation(com.sky.annotation.AutoFill)")  // 表示拦截 任意返回方法
    public void autoFillPointCut(){}

//    前置通知 再通知中进行公共字段的赋值
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("开始进行公共字段的填充");

//        需要做的事情

        //        需要获取当前拦截的操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();  // 获得是签名
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class); // 获得方法的注解对象

        OperationType operationType = autoFill.value();

//      获取当前被拦截的参数
        Object[] args = joinPoint.getArgs();

        if (args == null || args.length == 0) return;

//        准备赋值的数据
        Object entity = args[0];
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

//      根据当前的操作类型 通过对应的属性通过反射来赋值
        if (operationType == operationType.INSERT){
            try {

//               entity.getClass()  这是 JVM 在运行时告诉你的真实类型
//               如果 entity 是 Category clazz == Category.class
//               在“当前这个类”中，精确查找一个：
//               方法名叫 setCreateTime，参数类型是 LocalDateTime 的方法
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);

                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER , Long.class);

//                通过反射为对象属性赋值 是 实例方法，它必须有一个 this this.setCreateTime(...) 因此必须传入 entity
                setCreateTime.invoke(entity, now);
                setCreateUser.invoke(entity, currentId);
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        } else if (operationType == operationType.UPDATE) {
            try {

                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER , Long.class);

                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }


    }
}
