package com.allround.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationTest {
    public static void main(String[] args) throws Exception {
        Class<MyClass> clazz = MyClass.class;

        // 获取myMethod方法上的所有注解
        Method m = clazz.getDeclaredMethod("myMethod");
        Annotation[] annotations = m.getAnnotations();
        System.out.println(annotations.length);

        for (Annotation annotation : annotations){
            if (annotation instanceof MyAnnotation){
                MyAnnotation myAnnotation = (MyAnnotation) annotation;

                // 输出注解的值
                System.out.println("Value of the annotation: " + myAnnotation.value());
            }
        }
    }
}
