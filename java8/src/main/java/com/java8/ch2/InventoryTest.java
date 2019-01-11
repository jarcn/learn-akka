package com.java8.ch2;

import com.java8.bean.Apple;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author chenjia on 2019/1/12
 */
public class InventoryTest {

    public static void main(String... args) {
        List<Apple> inventory = Arrays.asList(new Apple(80, "green"),
          new Apple(155, "green"),
          new Apple(120, "red"));

        //定义分析行为方法（策略模式）
        List<Apple> filter = filter(inventory, new AppleGreenColorPredicate());
        System.out.println(filter);

        //定义分析行为方法（策略模式）
        List<Apple> filter1 = filter(inventory, new AppleHeavyWeightPredicate());
        System.out.println(filter1);

        //匿名内部类
        List<Apple> filter2 = filter(inventory, new ApplePredicate<Apple>() {
            @Override
            public boolean test(Apple apple) {
                return "red".equals(apple.getColor());
            }
        });
        System.out.println(filter2);

        //组合过滤器
        List<Apple> filter3 = filter2(inventory, apple -> "green".equals(apple.getColor()) && apple.getWeight() > 150);
        System.out.println(filter3);

        System.out.println(inventory);
        inventory.sort((apple1, apple2) -> apple1.getWeight().compareTo(apple2.getWeight()));
        System.out.println(inventory);

    }

    //自定过滤器
    private static List<Apple> filter(List<Apple> inventory, ApplePredicate<Apple> predicate) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (predicate.test(apple)) {
                result.add(apple);
            }
        }
        return result;
    }

    //抽象过滤器 通用代码
    public static <T> List<T> filter2(List<T> list, Predicate<T> p) {
        List<T> result = new ArrayList<>();
        for (T e : list) {
            if (p.test(e)) {
                result.add(e);
            }
        }
        return result;
    }

}
