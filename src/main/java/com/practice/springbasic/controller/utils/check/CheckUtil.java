package com.practice.springbasic.controller.utils.check;

public class CheckUtil {
    public static void bindingResultCheck(boolean bindingResultHasError) {
        if(bindingResultHasError) {throw new IllegalArgumentException("입력 값이 잘못되었습니다!");}
    }

    public static void nullCheck(Object checkValue) {
        if(checkValue == null) {throw new IllegalArgumentException("찾으려는 내용이 존재하지 않습니다!");}
    }
}
