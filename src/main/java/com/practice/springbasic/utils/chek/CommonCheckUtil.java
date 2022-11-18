package com.practice.springbasic.utils.chek;
import java.util.Arrays;

public class CommonCheckUtil {
    public static void nullCheck400(Object checkValue, String msg) {
        if(checkValue == null) {throw new IllegalArgumentException(msg);}
    }

    public static void nullCheck404(Object checkValue, String msg) {
        if(checkValue == null) {throw new NullPointerException(msg);}
    }

    public static void duplicateCheck400(boolean checkValue, String msg) {
        if(checkValue) {throw new IllegalArgumentException(msg);}
    }
}
