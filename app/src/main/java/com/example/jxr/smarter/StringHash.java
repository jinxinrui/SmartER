package com.example.jxr.smarter;

import java.security.MessageDigest;

/**
 * Created by jxr on 26/4/18.
 */

public class StringHash {

    private static MessageDigest md;
    // MD5 hash
    public static String hashPassword(String password) {
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] passBytes = password.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuffer sb = new StringBuffer();
            for(int i=0;i<digested.length;i++){
                sb.append(Integer.toHexString(0xff & digested[i]));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
