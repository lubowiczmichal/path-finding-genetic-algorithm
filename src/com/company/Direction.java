package com.company;

import java.util.Random;

public enum Direction{
    right, left, up, down;

    private static final Direction[] VALUES = values();
    private static final int SIZE = VALUES.length;
    private static final Random RANDOM = new Random();

    public static Direction random(){
        return VALUES[RANDOM.nextInt(SIZE)];
    }

}