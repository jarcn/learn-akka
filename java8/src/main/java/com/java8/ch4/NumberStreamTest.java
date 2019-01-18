package com.java8.ch4;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author chenjia on 2019/1/18
 */
public class NumberStreamTest {

    public static void main(String... args) {
        IntStream intStream = IntStream.rangeClosed(1, 100).filter(num -> num % 2 == 0);
        intStream.forEach(num -> System.out.print(num + ","));
        System.out.println("------------------------------");
        IntStream intStream1 = IntStream.range(1, 100).filter(num -> num % 2 == 0);
        intStream1.forEach(num -> System.out.print(num + ","));
        System.out.println("------------------------------");
        //筛选出1到100中所有勾股数组合
        Stream<int[]> pythagoreanTriples =
          IntStream.rangeClosed(1, 100)
            .boxed()
            .flatMap(a ->
              IntStream.rangeClosed(a, 100)
                .filter(b -> Math.sqrt(a * a + b * b) % 1 == 0)
                .mapToObj(b -> new int[] {a, b, (int) Math.sqrt(a * a + b * b)})
            );
        pythagoreanTriples.forEach(t -> System.out.println(t[0] + ", " + t[1] + ", " + t[2]));

    }


}
