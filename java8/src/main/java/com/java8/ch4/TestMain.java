package com.java8.ch4;

import java.util.stream.Stream;
import org.junit.Test;

/**
 * @author chenjia on 2019/1/18
 */
public class TestMain {

    @Test
    public void test1() {
        Stream.iterate(new long[] {0, 1},
          t -> new long[] {t[1], t[0] + t[1]})
          .limit(100)
          .map(t -> t[0])
          .forEach(t -> System.out.print(t + ","));
        System.out.println("7540113804746346429".length());
    }
}
