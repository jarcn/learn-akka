package com.java8.ch10;

import java.util.Optional;
import lombok.Data;

/**
 * @author chenjia on 2019/1/22
 */

@Data
public class Car {

    private Optional<Insurance> insurance;

}
