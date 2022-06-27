package com.ensa.ENSAPAY.security;

import java.util.Random;

public class PasswordGenerator {
    public static int len = 8;
    public static int smsLen = 5;
    public static String alphaNumericString() {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }
    public static String NumericString() {
        String AB = "0123456789";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder(smsLen);
        for (int i = 0; i < smsLen; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }
}
