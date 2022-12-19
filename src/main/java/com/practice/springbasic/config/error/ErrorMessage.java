package com.practice.springbasic.config.error;

public interface ErrorMessage {
    //Common 900
    String InternalServer = "900" + "@" + "내부 서버 오류, 잠시 후에 다시 시도해 주세요!";
    String NotFound = "910" + "@" + "요청하신 값을 찾을 수 없습니다!";
    String Forbidden = "920" + "@" + "경고, 정상적이지 않은 접근입니다!";
    String DataIntegrityViolation = "930" + "@" + "데이터 바인딩이 잘못되었습니다!";
    String Unknown = "940" + "@" + "알 수 없는 에러!";

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
    String JWTDecodeFailed = "1120" + "@" + "경고, 정상적이지 않은 토큰입니다.";
    String TokenExpired = "1130" + "@" + "만료된 토큰입니다.";
    String InvalidClaim = "1140" + "@" + "경고, 정상적이지 않은 토큰입니다.";
    String SignatureVerificationFailed = "1150" + "@" + "경고, 정상적이지 않은 토큰입니다.";
    String JWTVerificationFailed = "1160" + "@" + "경고, 정상적이지 않은 토큰입니다.";

    //Board 1200
    String BoardTitleEmpty = "1210" + "@" + "제목은 비어 있을 수 없습니다!";
    String BoardCategoryEmpty = "1220" + "@" + "카테고리는 비어 있을 수 없습니다!";
    String BoardContentEmpty = "1230" + "@" + "내용은 비어 있을 수 없습니다!";
    String BoardTitleLen = "1240" + "@" + "제목의 길이가 잘못되었습니다!";
    String BoardCategoryParse = "1250" + "@" + "경고, 잘못된 카테고리입니다!";
    String BoardContentLen = "1260" + "@" + "내용의 길이가 잘못되었습니다!";
    String BoardNotFound = "1270" + "@" + "존재하지 않는 게시글입니다!";
    //Comment 1300


}
