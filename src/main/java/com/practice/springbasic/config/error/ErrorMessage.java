package com.practice.springbasic.config.error;

public interface ErrorMessage {
    //Member 1000
    static final String LoginFailedByWrongInput = "1000" + "@" + "이메일 혹은 패스워드가 잘못되었습니다!";
    static final String EmailEmpty = "1010" + "@" + "이메일은 비어 있을 수 없습니다!";
    static final String EmailForm = "1020" + "@" + "올바른 이메일을 입력하세요!";
    static final String NicknameEmpty = "1030" + "@" + "닉네임은 비어 있을 수 없습니다!";
    static final String NicknameLen = "1040" + "@" + "닉네임 길이가 잘못되었습니다!";
    static final String PasswordEmpty = "1050" + "@" + "패스워드는 비어 있을 수 없습니다!";
    static final String PasswordLen = "1060" + "@" + "패스워드 길이가 잘못되었습니다!";
    static final String DuplicateEmail = "1070" + "@" + "중복된 이메일입니다!";
    static final String DuplicateNickname = "1080" + "@" + "중복된 닉네임입니다!";
    static final String MemberNotFound = "1090" + "@" + "존재하지 않는 사용자입니다!";

}
