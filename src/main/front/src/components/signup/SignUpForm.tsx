import React, {useState} from "react";
import classes from "./SIgnUpForm.module.css";
import {equal} from "assert";
import axios from "axios";
import {useNavigate} from "react-router-dom";

export default function SignUpForm(props: any) {

    const [enteredEmail, setEnteredEmail] = useState('');
    const [enteredNickname, setEnteredNickname] = useState('');
    const [enteredPassword, setEnteredPassword] = useState('');
    const [enteredRepeatPassword, setEnteredRepeatPassword] = useState('');
    const [passwordEqual, setPasswordEqual] = useState(false);
    const [emailDuplicateCheck, setEmailDuplicateCheck] = useState(false)
    const [nicknameDuplicateCheck, setNicknameDuplicateCheck] = useState(false)
    const [error, setError] = useState({title: "", message: ""});
    const navigate = useNavigate();


    const signUpRequest = (email: string, nickname: string, password: string) => {
        let url;
        url = "/members"
        axios.post(url, {
            email: email,
            nickname: nickname,
            password: password
        }).then(function (response) {
            if (response.status === 200) {
                setEmailDuplicateCheck(true);
                alert("회원가입에 성공하였습니다!");
                setEnteredEmail('');
                setEnteredNickname('');
                setEnteredPassword('');
                setEnteredRepeatPassword('');
                navigate("/home");
            } else {
                alert(response.data.message);
            }
        }).catch(function (error) {
            alert(error.response.data.message);
        });
    }

    const nicknameCheckRequest = (nickname: string) => {
        let url;
        url = "/members/nickname-check"
        axios.post(url, {
            nickname: nickname
        }).then(function (response) {
            if (response.status === 200) {
                setNicknameDuplicateCheck(true);
                alert("사용 가능한 닉네임입니다!");
            } else {
                alert(response.data.message);
            }
        }).catch(function (error) {
            alert(error.response.data.message);
        });
    }

    const emailCheckRequest = (email: string) => {
        let url;
        url = "/members/email-check"
        axios.post(url, {
            email: email
        }).then(function (response) {
            if (response.status === 200) {
                setEmailDuplicateCheck(true);
                alert("사용 가능한 이메일입니다!");
            } else {
                alert(response.data.message);
            }
        }).catch(function (error) {
            alert(error.response.data.message);
        });
    }

    const addUserHandler = (event: React.FormEvent) => {
        event.preventDefault();
        if (enteredNickname.trim().length === 0 || enteredNickname.trim().length === 0) {
            setError({
                title: 'Invalid input',
                message: 'Please enter a valid name and age (non-empty values).',
            });
            return;
        }
        signUpRequest(enteredEmail, enteredNickname, enteredPassword);
    };

    const emailChangeHandler = (event: React.FormEvent<HTMLInputElement>) => {
        setEnteredEmail(event.currentTarget.value);
        setEmailDuplicateCheck(false)
    };

    const nicknameChangeHandler = (event: React.FormEvent<HTMLInputElement>) => {
        setEnteredNickname(event.currentTarget.value);
        setNicknameDuplicateCheck(false)
    };

    const passwordChangeHandler = (event: React.FormEvent<HTMLInputElement>) => {
        setEnteredPassword(event.currentTarget.value);
        setPasswordEqual(false)
    };

    const repeatPasswordChangeHandler = (event: React.FormEvent<HTMLInputElement>) => {
        setEnteredRepeatPassword(event.currentTarget.value);
        setPasswordEqual(false)
    };

    const emailDuplicateCheckHandler = (event: React.FormEvent<HTMLInputElement>) => {
        emailCheckRequest(enteredEmail);
    }

    const nicknameDuplicateCheckHandler = (event: React.FormEvent<HTMLInputElement>) => {
        nicknameCheckRequest(enteredNickname);
    }

    const equalPasswordCheckHandler = (event: any) => {
        if (enteredPassword.trim().length === 0) {
            alert("패스워드를 입력해 주세요!")
            return
        }

        if (enteredRepeatPassword.trim().length === 0) {
            alert("검증 패스워드를 입력해 주세요!")
            return
        }

        if (enteredPassword !== enteredRepeatPassword) {
            alert("패스워드가 일치하지 않습니다!")
            return
        }
        // eslint-disable-next-line eqeqeq
        if (enteredPassword === enteredRepeatPassword) {
            setPasswordEqual(true);
            alert("패스워드 확인 완료!");
        }
    }

    const errorHandler = () => {
        // @ts-ignore
        setError(null);
    };

    return (
        <>
            <div className={classes.input}>
                {/*{error && (*/}
                {/*    <ErrorModal*/}
                {/*        title={error.title}*/}
                {/*        message={error.message}*/}
                {/*        onConfirm={errorHandler}*/}
                {/*    />*/}
                {/*)}*/}
                {/*<Card className={classes.input}>*/}
                <form onSubmit={addUserHandler}>
                    <label htmlFor="email">Email</label>
                    <input
                        id="email"
                        type="email"
                        required={true}
                        value={enteredEmail}
                        onChange={emailChangeHandler}
                    />
                    {!emailDuplicateCheck ?
                        <input type="button" onClick={emailDuplicateCheckHandler} value={"이메일 중복 체크"}/> :
                        <input type="button" value={"사용 가능한 이메일입니다!"}/>}
                    <label htmlFor="nickname">Nickname</label>
                    <input
                        id="nickname"
                        type="text"
                        required={true}
                        value={enteredNickname}
                        onChange={nicknameChangeHandler}
                    />
                    {!nicknameDuplicateCheck ?
                        <input type="button" onClick={nicknameDuplicateCheckHandler} value={"닉네임 중복 체크"}/> :
                        <input type="button" value={"사용 가능한 닉네임입니다!"}/>}
                    <label htmlFor="password">Password</label>
                    <input
                        id="password"
                        type="text"
                        required={true}
                        value={enteredPassword}
                        onChange={passwordChangeHandler}
                    />
                    <label htmlFor="repeat_password">Repeat Password</label>
                    <input
                        id="repeat_password"
                        type="text"
                        value={enteredRepeatPassword}
                        onChange={repeatPasswordChangeHandler}
                    />
                    <input type="button" onClick={equalPasswordCheckHandler} value={"비밀번호확인"}/>
                    {/*<button type="button" onClick={equalPasswordCheckHandler}>비밀번호 확인</button>*/}
                    {nicknameDuplicateCheck && emailDuplicateCheck && passwordEqual &&
                        <button type="submit">Add User</button>}
                </form>
                {/*</Card>*/}
            </div>
        </>
    );
}