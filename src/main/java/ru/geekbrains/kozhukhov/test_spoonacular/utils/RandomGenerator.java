package ru.geekbrains.kozhukhov.test_spoonacular.utils;

import java.util.Random;

public class RandomGenerator {

    public static final int LETTER_a_CODE = 97;
    public static final int LETTER_z_CODE = 122;
    public static final int RANDOM_STRING_LENGTH = 10;

    public static String username(){
        Random random = new Random();

        return random.ints(LETTER_a_CODE, LETTER_z_CODE + 1)
                .limit(RANDOM_STRING_LENGTH)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

}
