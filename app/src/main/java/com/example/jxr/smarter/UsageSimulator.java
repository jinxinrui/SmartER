package com.example.jxr.smarter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
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
        NumberFormat formatter = new DecimalFormat("#0.00");
        String numString = formatter.format(n/100);
        double number = Double.parseDouble(numString);
        return number;
    }

    public double randomAir() {
        Random random = new Random();
        double n = random.nextInt(5) + 1;
        NumberFormat formatter = new DecimalFormat("#0.00");
        String numString = formatter.format(n);
        double number = Double.parseDouble(numString);
        return number;
    }

    public double randomWash() {
        Random random = new Random();
        int n = random.nextInt(13) + 4;
        NumberFormat formatter = new DecimalFormat("#0.00");
        String numString = formatter.format(n/10);
        double number = Double.parseDouble(numString);
        return number;
    }
}
