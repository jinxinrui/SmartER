package com.example.jxr.smarter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

/**
 * Created by jxr on 25/4/18.
 */

public class UsageSimulator {

    public UsageSimulator() {
    }

    // frige usage range 0.30~0.80
    public static String randomFridge() {
        Random random = new Random();
        double n = 0.30 + (0.80-0.30) * random.nextDouble();
        NumberFormat formatter = new DecimalFormat("#0.00");
        String numString = formatter.format(n);
        //double number = Double.parseDouble(numString);
        return numString;
    }

    // airCon usage range
    public static String randomAir() {
        Random random = new Random();
        double n = 4 * random.nextDouble() + 1.00;
        NumberFormat formatter = new DecimalFormat("#0.00");
        String numString = formatter.format(n);
        //double number = Double.parseDouble(numString);
        return numString;
    }

    public static String randomWash() {
        Random random = new Random();
        double n = (1.3-0.4) * random.nextDouble() + 0.4;
        NumberFormat formatter = new DecimalFormat("#0.00");
        String numString = formatter.format(n);
        //double number = Double.parseDouble(numString);
        return numString;
    }
}
