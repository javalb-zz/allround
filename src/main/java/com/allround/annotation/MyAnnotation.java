package com.allround.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//指定注解作用范围（比如说：该注解是作用在类上，还是方法，或者是属性上等等）
@Retention(RetentionPolicy.RUNTIME)//指定注解的生命周期（也就是注解的保留时间，是在编译器有效，还是运行时有效等等）
public @interface MyAnnotation {
    String value() default "";
}
