package com.practice.springbasic.utils.chek;

import org.springframework.web.client.HttpClientErrorException;

public class CommonCheckUtil {
    public static void nullCheck401(Object checkValue, String msg) {
        if(checkValue == null) {throw new IllegalArgumentException(msg);}
    }

    public static void nullCheck404(Object checkValue, String msg) {
        if(checkValue == null) {throw new NullPointerException(msg);}
    }
}
