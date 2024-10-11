package com.allround.basic.stream;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class StreamDemo {
    public static void main(String[] args) {
        StreamDemo demo = new StreamDemo();
        demo.testone();
        demo.testtwo();
        demo.testthree();
        demo.testfour();
        demo.testfive();
        demo.testsix();
        demo.testseven();
        demo.testeight();
    }

    public void testone() {
        List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);

        IntSummaryStatistics stats = numbers.stream().mapToInt((x) -> x).summaryStatistics();

        System.out.println("列表中最大的数 : " + stats.getMax());
        System.out.println("列表中最小的数 : " + stats.getMin());
        System.out.println("所有数之和 : " + stats.getSum());
        System.out.println("平均数 : " + stats.getAverage());
    }

    public void testtwo() {
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
        List<String> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
        System.out.println(filtered);
    }

    public void testthree() {
        Random random = new Random();
        random.ints(1,100).limit(10).forEach(System.out::println);
    }

    public void testfour() {
        //map 方法用于映射每个元素到对应的结果，以下代码片段使用 map 输出了元素对应的平方数：
        List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
// 获取对应的平方数
        List<Integer> squaresList = numbers.stream().map( i -> i*i).distinct().collect(Collectors.toList());
        System.out.println(squaresList);
    }

    public void testfive() {
        //filter 方法用于通过设置的条件过滤出元素。以下代码片段使用 filter 方法过滤出空字符串：
        List<String>strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
// 获取空字符串的数量
        long count = strings.stream().filter(string -> string.isEmpty()).count();
        System.out.println("空字符串的数量为: "+count);
    }

    public void testsix() {
        //parallelStream 是流并行处理程序的代替方法。以下实例我们使用 parallelStream 来输出空字符串的数量：
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
// 获取空字符串的数量
        long count = strings.parallelStream().filter(string -> string.isEmpty()).count();
        System.out.println("空字符串的数量为: "+count);
    }

    //sorted 方法用于对流进行排序。以下代码片段使用 sorted 方法对输出的 10 个随机数进行排序：
    public void testseven() {
        Random random = new Random();
        random.ints(1,100).limit(10).sorted().forEach(System.out::println);
    }

    //Collectors 类实现了很多归约操作，例如将流转换成集合和聚合元素。Collectors 可用于返回列表或字符串：
    public void testeight() {
        List<String>strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
        List<String> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());

        System.out.println("筛选列表: " + filtered);
        String mergedString = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.joining(", "));
        System.out.println("合并字符串: " + mergedString);
    }
}
