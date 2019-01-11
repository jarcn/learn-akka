package com.java8.bean;

import lombok.Data;

/**
 * @author chenjia on 2019/1/11
 */
@Data
public class Apple {

    private String color;
    private Integer weight;

    public Apple(Integer weight, String color) {
        this.color = color;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Apple{" +
          "color='" + color + '\'' +
          ", weight=" + weight +
          '}';
    }

}
