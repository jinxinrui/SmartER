package com.example.jxr.smarter;

import java.security.MessageDigest;

/**
 * Created by jxr on 26/4/18.
 */

public class StringHash {
    // MD5 hash
    public static String hashPass(String password) {
        byte[] bytesOfMessage = null;
        MessageDigest md = null;
        try {
            bytesOfMessage = password.getBytes("UTF-8");
            md = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.valueOf(md.digest(bytesOfMessage));
    }
}
