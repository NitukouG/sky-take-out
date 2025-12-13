package com.sky.annotation;


import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 自定义注解 用于标识某个方法 自定义填充处理
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)  // 声明一个“自定义注解能存活到什么时候”
public @interface AutoFill {
//    赋值操作类型 自定义枚举变量
    OperationType value();
}
