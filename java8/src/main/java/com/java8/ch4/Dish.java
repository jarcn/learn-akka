package com.java8.ch4;

import lombok.Data;

/**
 * @author chenjia on 2019/1/18
 */
@Data
public class Dish {

    private int calories;

    public Dish(int calories) {
        this.calories = calories;
    }

    public Dish() {
    }

}
