package com.example.polls.task;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {
    private static final Random RANDOM = new Random();

    public static <T> T getRandomItem(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("The list must not be null or empty");
        }
        int index =  ThreadLocalRandom.current().nextInt(list.size());
        return list.get(index);
    }
}

