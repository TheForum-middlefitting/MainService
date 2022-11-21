package com.practice.springbasic.config.error;

public interface ErrorMessage {
    //Member 1000
    String LoginFailedByWrongInput = "1000" + "@" + "이메일 혹은 패스워드가 잘못되었습니다!";
    String EmailEmpty = "1010" + "@" + "이메일은 비어 있을 수 없습니다!";
    String EmailForm = "1020" + "@" + "올바른 이메일을 입력하세요!";
    String NicknameEmpty = "1030" + "@" + "닉네임은 비어 있을 수 없습니다!";
    String NicknameLen = "1040" + "@" + "닉네임 길이가 잘못되었습니다!";
    String PasswordEmpty = "1050" + "@" + "패스워드는 비어 있을 수 없습니다!";
    String PasswordLen = "1060" + "@" + "패스워드 길이가 잘못되었습니다!";
    String DuplicateEmail = "1070" + "@" + "중복된 이메일입니다!";
    String DuplicateNickname = "1080" + "@" + "중복된 닉네임입니다!";
    String MemberNotFound = "1090" + "@" + "존재하지 않는 사용자입니다!";
    String PasswordWrong = "1010" + "@" + "패스워드가 틀렸습니다!";


    //Auth 1100
    String NoToken = "1100" + "@" + "토큰이 존재하지 않습니다!";
    String AuthFailed = "1110" + "@" + "경고. 권한이 없는 사용자입니다!";
}
