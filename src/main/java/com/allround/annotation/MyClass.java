package com.allround.annotation;

public class MyClass {

    @MyAnnotation(value = "Hello")
    public void myMethod(){
        System.out.println("This is a method with custom annotation.");
    }
}
