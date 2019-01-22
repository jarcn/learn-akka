package com.java8.ch10;

import java.util.Optional;

/**
 * @author chenjia on 2019/1/23
 */
public class TestMain {

    public static void main(String... args) {

        String carInsuranceName = getCarInsuranceName(Optional.of(new Person()));
        System.out.println(carInsuranceName);

    }


    public static String getCarInsuranceName(Optional<Person> person) {
        return person.flatMap(Person::getCar)
          .flatMap(Car::getInsurance)
          .map(Insurance::getName)
          .orElse("Unknown");
    }
}
