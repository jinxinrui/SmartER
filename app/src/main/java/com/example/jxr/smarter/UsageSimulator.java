package com.example.jxr.smarter;

import java.util.Random;

/**
 * Created by jxr on 25/4/18.
 */

public class UsageSimulator {
    private String acUsage;
    private String fridgeUsage;
    private String washUsage;

    public UsageSimulator() {
    }

    public double randomFridge() {
        Random random = new Random();
        int n = random.nextInt(50) + 30;
        return n / 100;
    }

    public double randomAir() {
        Random random = new Random();
        double n = random.nextInt(5) + 1;
        return n;
    }

    public double randomWash() {
        Random random = new Random();
        int n = random.nextInt(13) + 4;
        return n / 10;
    }
}
