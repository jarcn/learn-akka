package com.java8.ch1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author chenjia on 2019/1/11
 */
public class AppleTest {

    public static void main(String... args) {

        List<Apple> inventory = Arrays.asList(new Apple(80, "green"),
          new Apple(155, "green"),
          new Apple(120, "red"));

        //过滤所有颜色为green的apple
        List<Apple> greenApples = filterApples(inventory, AppleTest::isGreenApple);
        System.out.println(greenApples);

        //过滤所有重量大于150的apple
        List<Apple> apples = filterApples(inventory, AppleTest::isHeavyApple);
        System.out.println(apples);

        //将谓词函数中的行为使用lamada表达式 更简化代码
        List<Apple> greenApples2 = filterApples(inventory, apple -> "green".equals(apple.getColor()));
        System.out.println(greenApples2);

        List<Apple> apples1 = filterApples(inventory, apple -> apple.getWeight() > 150);
        System.out.println(apples1);

    }

    /**
     * 定义一个行为
     */
    private static boolean isGreenApple(Apple apple) {
        return "green".equals(apple.getColor());
    }

    /**
     * 定义一个行为
     */
    private static boolean isHeavyApple(Apple apple) {
        return apple.getWeight() > 150;
    }

    /**
     * 定义一个谓词函数
     */
    private static List<Apple> filterApples(List<Apple> inventory, Predicate<Apple> predicate) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (predicate.test(apple)) {
                result.add(apple);
            }
        }
        return result;
    }
}
