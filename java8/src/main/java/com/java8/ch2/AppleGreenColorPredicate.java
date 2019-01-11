package com.java8.ch2;

import com.java8.bean.Apple;

/**
 * @author chenjia on 2019/1/12
 */
public class AppleGreenColorPredicate implements ApplePredicate<Apple> {

    @Override
    public boolean test(Apple apple) {
        return "green".equals(apple.getColor());
    }
}
