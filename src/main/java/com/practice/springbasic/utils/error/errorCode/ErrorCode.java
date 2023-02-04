package com.practice.springbasic.utils.error.errorCode;

import lombok.Getter;

@Getter
//@ToString
public enum ErrorCode {
    //Common 900
    InternalServer(900, "내부 서버 오류, 잠시 후에 다시 시도해 주세요!"),
    NotFound(910, "요청하신 값을 찾을 수 없습니다!"),
    Forbidden(920, "경고, 정상적이지 않은 접근입니다!"),
    DataIntegrityViolation(930, "데이터 바인딩이 잘못되었습니다!"),
    Unknown(940, "알 수 없는 에러!"),
    //Member 1000
    LoginFailedByWrongInput(1000, "이메일 혹은 패스워드가 잘못되었습니다!"),
    EmailEmpty(1010, "이메일은 비어 있을 수 없습니다!"),
    EmailForm(1020, "올바른 이메일을 입력하세요!"),
    NicknameEmpty(1030, "닉네임은 비어 있을 수 없습니다!"),
    NicknameLen(1040, "닉네임 길이가 잘못되었습니다!"),
    PasswordEmpty(1050, "패스워드는 비어 있을 수 없습니다!"),
    PasswordLen(1060, "패스워드 길이가 잘못되었습니다!"),
    DuplicateEmail(1070, "중복된 이메일입니다!"),
    DuplicateNickname(1080, "중복된 닉네임입니다!"),
    MemberNotFound(1090, "존재하지 않는 사용자입니다!"),
    PasswordWrong(1091, "패스워드가 틀렸습니다!"),
    EmailNotFound(1092, "존재하지 않는 이메일입니다!"),
    //Auth 1100
    NoToken(1100, "토큰이 존재하지 않습니다!"),
    AuthFailed(1110, "경고. 권한이 없는 사용자입니다!"),
    JWTDecodeFailed(1120, "경고, 정상적이지 않은 토큰입니다."),
    TokenExpired(1130, "만료된 토큰입니다."),
    InvalidClaim(1140, "경고, 정상적이지 않은 토큰입니다."),
    SignatureVerificationFailed(1150, "경고, 정상적이지 않은 토큰입니다."),
    JWTVerificationFailed(1160, "경고, 정상적이지 않은 토큰입니다."),
    //Board 1200
    BoardTitleEmpty(1210, "제목은 비어 있을 수 없습니다!"),
    BoardCategoryEmpty(1220, "카테고리는 비어 있을 수 없습니다!"),
    BoardContentEmpty(1230, "내용은 비어 있을 수 없습니다!"),
    BoardTitleLen(1240, "제목의 길이가 잘못되었습니다!"),
    BoardCategoryParse(1250, "경고, 잘못된 카테고리입니다!"),
    BoardContentLen(1260, "내용의 길이가 잘못되었습니다!"),
    BoardNotFound(1270, "존재하지 않는 게시글입니다");
    //Comment 1300
    private final int code;
    private final String message;

    private ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return this.getCode() + "@" + this.getMessage();
    }
}
