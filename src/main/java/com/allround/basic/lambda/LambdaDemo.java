package com.allround.basic.lambda;

import java.util.Arrays;
import java.util.List;

/**
 * lambda表达式示例
 */
public class LambdaDemo {
    public static void main(String args[]){
        LambdaDemo tester = new LambdaDemo();
        tester.testone();
        tester.testtwo();
        tester.testthree();
        tester.testfour();
    }

    interface MathOperation {
        int operation(int a, int b);
    }

    interface GreetingService {
        void sayMessage(String message);
    }
    public void testone(){
        MathOperation addition = (int a, int b) -> a + b;
        System.out.println(addition.operation(10, 5));
    }

    public void testtwo(){
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        names.forEach(name -> System.out.println(name));
    }

    public void testthree(){
        // 使用 Lambda 表达式和 Stream API 进行并行计算
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5,6, 7, 8, 9, 10);
        int sum = numbers.parallelStream().mapToInt(Integer::intValue).sum();
        System.out.println(sum);
    }
    public void testfour(){
        // 不用括号
        GreetingService greetService1 = message ->
                System.out.println("Hello " + message);

        // 用括号
        GreetingService greetService2 = (message) ->
                System.out.println("Hello " + message);

        greetService1.sayMessage("Runoob");
        greetService2.sayMessage("Google");
    }
}
