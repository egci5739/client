package com.dyw.client.controller;

import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {
        String content = "9087656";

        String pattern = "^[1-9]\\d*$";

        boolean isMatch = Pattern.matches(pattern, content);
        System.out.println(isMatch);
    }
}
