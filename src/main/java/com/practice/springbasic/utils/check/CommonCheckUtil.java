package com.practice.springbasic.utils.check;
import com.practice.springbasic.utils.error.exception.AuthenticationFailedException;

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

    public static void equalCheck401(Object firstComparator, Object secondComparator, String msg) {
        if(!firstComparator.equals(secondComparator)) {
            throw new AuthenticationFailedException(msg);
        }
    }

    public static void booleanCheck400(boolean checkValue, String msg) {
        if(!checkValue) {throw new IllegalArgumentException(msg);}
    }
}
