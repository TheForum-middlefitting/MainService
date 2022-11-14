import React, {useContext, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import AuthContext from "../../store/context/auth-context";
import Form from "react-bootstrap/Form";
import {Container} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import {errorActions} from "../../store/redux/errorSlice";
import {alertActions} from "../../store/redux/alertSlice";
import {warningActions} from "../../store/redux/warningSlice";
import {useDispatch} from "react-redux";
import {useMutation} from "react-query";
import {emailCheckRequest, nicknameCheckRequest, signUpRequest} from "../../request/usersRequest";

export default function SignUpForm(props: any) {

    const [enteredEmail, setEnteredEmail] = useState('');
    const [enteredNickname, setEnteredNickname] = useState('');
    const [enteredPassword, setEnteredPassword] = useState('');
    const [enteredRepeatPassword, setEnteredRepeatPassword] = useState('');
    const [passwordEqual, setPasswordEqual] = useState(false);
    const [emailDuplicateCheck, setEmailDuplicateCheck] = useState(false)
    const [nicknameDuplicateCheck, setNicknameDuplicateCheck] = useState(false)
    const [emailEntered, setEmailEntered] = useState(false);
    const [nicknameEntered, setNicknameEntered] = useState(false);
    const [passwordEntered, setPasswordEntered] = useState(false);
    const navigate = useNavigate();
    const authCtx = useContext(AuthContext);
    const isLogin = authCtx.isLoggedIn;
    const dispatch = useDispatch();

    useEffect(() => {
        if (isLogin) {
            navigate("/home")
        }
    }, [isLogin]);


    const signUpMutation = useMutation((params : any ) => signUpRequest(params.email, params.nickname, params.password), {
        onSuccess: (response) => {
            setEmailDuplicateCheck(true);
            setEnteredEmail('');
            setEnteredNickname('');
            setEnteredPassword('');
            setEnteredRepeatPassword('');
            navigate("/home");
            dispatch(alertActions.setMessage("회원가입을 완료하였습니다!"));
            dispatch(alertActions.open());
        },
        onError: (error : any) => {
            dispatch(errorActions.setStatus(error.response.data.status));
            dispatch(errorActions.setMessage(error.response.data.message));
            dispatch(errorActions.setCode(error.response.data.code));
            dispatch(errorActions.open());
        },
    });

    const emailCheckMutation = useMutation((params: string ) => emailCheckRequest(params), {
        onSuccess: (response) => {
            setEmailDuplicateCheck(true);
        },
        onError: (error : any) => {
            dispatch(alertActions.setMessage(error.response.data.message));
            dispatch(alertActions.open());
        },
    });

    const nicknameCheckMutation = useMutation((params: string ) => nicknameCheckRequest(params), {
        onSuccess: (response) => {
            setNicknameDuplicateCheck(true);
        },
        onError: (error : any) => {
            dispatch(alertActions.setMessage(error.response.data.message));
            dispatch(alertActions.open());
        },
    });


    const addUserHandler = (event: React.FormEvent) => {
        event.preventDefault();
        if (enteredNickname.trim().length === 0 || enteredNickname.trim().length === 0) {
            return;
        }
        const params = {
            email: enteredEmail.trim(),
            nickname: enteredNickname.trim(),
            password: enteredPassword.trim(),
        }
        signUpMutation.mutate(params)
    };


    const emailChangeHandler = (event: any) => {
        setEnteredEmail(event.currentTarget.value);
        setEmailDuplicateCheck(false)
        setEmailEntered(true)
    };

    const nicknameChangeHandler = (event: any) => {
        setEnteredNickname(event.currentTarget.value);
        setNicknameDuplicateCheck(false)
        setNicknameEntered(true)
    };

    const passwordChangeHandler = (event: any) => {
        setEnteredPassword(event.currentTarget.value);
        setPasswordEqual(false)
        setPasswordEntered(true)
    };

    const repeatPasswordChangeHandler = (event: any) => {
        setEnteredRepeatPassword(event.currentTarget.value);
        setPasswordEqual(false)
    };

    const emailDuplicateCheckHandler = (event: any) => {
        if (!emailParseCheckHandler()) {
            return
        }
        emailCheckMutation.mutate(enteredEmail.trim())
    }

    const nicknameDuplicateCheckHandler = (event: any) => {
        if (!nicknameParseCheckHandler()) {
            return
        }
        nicknameCheckMutation.mutate(enteredNickname.trim());
    }

    const equalPasswordCheckHandler = (event: any) => {
        if (!passwordParseCheckHandler()) {
            return
        }

        if (enteredRepeatPassword.trim().length === 0) {
            dispatch(alertActions.setMessage("검증 비밀번호를 입력해주세요!"));
            dispatch(alertActions.open());
            return
        }

        if (enteredPassword.trim() !== enteredRepeatPassword.trim()) {
            dispatch(alertActions.setMessage("비밀번호가 일치하지 않습니다!"));
            dispatch(alertActions.open());
            return
        }
        if (enteredPassword === enteredRepeatPassword) {
            setPasswordEqual(true);
        }
    }

    const emailParseCheckHandler = () => {
        const regex = /([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
        return regex.test(enteredEmail.trim())
    }

    const nicknameParseCheckHandler = () => {
        if (enteredNickname.trim().length < 4 || enteredNickname.trim().length > 20) {
            return false
        }
        return true
    }

    const passwordParseCheckHandler = () => {
        if (enteredPassword.trim().length < 10 || enteredPassword.trim().length > 20) {
            return false
        }
        return true
    }

    const undoHandler = () => {
        navigate("/home");
    }

    const undoCheck = () => {
        dispatch(warningActions.setMessage("회원가입을 취소합니다"));
        dispatch(warningActions.open());
        dispatch(warningActions.setFunction(undoHandler));
    }

    return (
        <>
            <Container
                className={"mx-auto my-3 container-sm"}
                style={{maxWidth: "540px"}}
            >
                <br/>
                <h2 className={"text-center"}>
                    <strong>회원가입</strong>
                </h2>
                <Form onSubmit={addUserHandler}>
                    <Form.Group className="mb-3">
                        <Form.Label><strong>아이디</strong></Form.Label>
                        <Form.Control
                            placeholder="이메일을 입력해 주세요"
                            id="email"
                            type="email"
                            required={true}
                            value={enteredEmail}
                            onChange={emailChangeHandler}
                            isValid={emailEntered && emailParseCheckHandler()}
                            isInvalid={emailEntered && !emailParseCheckHandler()}
                        />
                        <Form.Control.Feedback>올바른 형식입니다</Form.Control.Feedback>
                        <Form.Control.Feedback type="invalid">
                            올바른 이메일 형식을 입력해 주세요!
                        </Form.Control.Feedback>
                    </Form.Group>
                    <div className="d-grid mb-3">
                        <Button type="button" variant={"outline-primary"} onClick={emailDuplicateCheckHandler}
                                disabled={emailDuplicateCheck}>
                            {emailDuplicateCheck ? "사용 가능한 이메일입니다!" : "이메일 중복 체크"}
                        </Button>
                    </div>

                    <Form.Group className="mb-3">
                        <Form.Label><strong>닉네임</strong></Form.Label>
                        <Form.Control
                            placeholder="닉네임을 입력해 주세요"
                            id="nickname"
                            type="text"
                            required={true}
                            value={enteredNickname}
                            onChange={nicknameChangeHandler}
                            isValid={nicknameEntered && nicknameParseCheckHandler()}
                            isInvalid={nicknameEntered && !nicknameParseCheckHandler()}
                        />
                        <Form.Control.Feedback>올바른 형식입니다</Form.Control.Feedback>
                        <Form.Control.Feedback type="invalid">
                            닉네임은 4 ~ 20 글자 사이로 입력해 주세요!
                        </Form.Control.Feedback>
                    </Form.Group>
                    <div className="d-grid mb-3">
                        <Button type="button" variant={"outline-primary"} onClick={nicknameDuplicateCheckHandler}
                                disabled={nicknameDuplicateCheck}>
                            {nicknameDuplicateCheck ? "사용 가능한 닉네임입니다!" : "닉네임 중복 체크"}
                        </Button>
                    </div>

                    <Form.Group className="mb-3">
                        <Form.Label><strong>비밀번호</strong></Form.Label>
                        <Form.Control
                            placeholder="비밀번호를 입력해 주세요"
                            id="password"
                            type="password"
                            required={true}
                            value={enteredPassword}
                            onChange={passwordChangeHandler}
                            isValid={passwordEntered && passwordParseCheckHandler()}
                            isInvalid={passwordEntered && !passwordParseCheckHandler()}
                        />
                        <Form.Control.Feedback>올바른 형식입니다</Form.Control.Feedback>
                        <Form.Control.Feedback type="invalid">
                            비밀번호는 10 ~ 20 글자 사이로 입력해 주세요!
                        </Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Control
                            placeholder="검증 비밀번호를 입력해 주세요"
                            id="repeat_password"
                            type="password"
                            value={enteredRepeatPassword}
                            onChange={repeatPasswordChangeHandler}
                        />
                    </Form.Group>
                    <div className="d-grid mb-5">
                        <Button type="button" variant={"outline-primary"} onClick={equalPasswordCheckHandler}
                                disabled={passwordEqual}>
                            {passwordEqual ? "동일한 비밀번호입니다!" : "비밀번호 확인"}
                        </Button>
                    </div>
                    <div className="d-grid mb-3">
                        <Button
                            type="submit"
                            disabled={!(nicknameDuplicateCheck && emailDuplicateCheck && passwordEqual)}>
                            회원가입
                        </Button>
                    </div>
                    <div className="d-grid mb-3">
                        <Button
                            variant={"outline-danger"}
                            onClick={undoCheck}>
                            취소
                        </Button>
                    </div>
                </Form>
            </Container>
        </>
    );
}

{/*<div className={classes.input}>*/
}
{/*    <form onSubmit={addUserHandler}>*/
}
{/*        <label htmlFor="email">Email</label>*/
}
{/*        <input*/
}
{/*            id="email"*/
}
{/*            type="email"*/
}
{/*            required={true}*/
}
{/*            value={enteredEmail}*/
}
{/*            onChange={emailChangeHandler}*/
}
{/*        />*/
}
{/*        {!emailDuplicateCheck ?*/
}
{/*            <input type="button" onClick={emailDuplicateCheckHandler} value={"이메일 중복 체크"}/> :*/
}
{/*            <input type="button" value={"사용 가능한 이메일입니다!"}/>}*/
}
{/*        <label htmlFor="nickname">Nickname</label>*/
}
{/*        <input*/
}
{/*            id="nickname"*/
}
{/*            type="text"*/
}
{/*            required={true}*/
}
{/*            value={enteredNickname}*/
}
{/*            onChange={nicknameChangeHandler}*/
}
{/*        />*/
}
{/*        {!nicknameDuplicateCheck ?*/
}
{/*            <input type="button" onClick={nicknameDuplicateCheckHandler} value={"닉네임 중복 체크"}/> :*/
}
{/*            <input type="button" value={"사용 가능한 닉네임입니다!"}/>}*/
}
{/*        <label htmlFor="password">Password</label>*/
}
{/*        <input*/
}
{/*            id="password"*/
}
{/*            type="text"*/
}
{/*            required={true}*/
}
{/*            value={enteredPassword}*/
}
{/*            onChange={passwordChangeHandler}*/
}
{/*        />*/
}
{/*        <label htmlFor="repeat_password">Repeat Password</label>*/
}
{/*        <input*/
}
{/*            id="repeat_password"*/
}
{/*            type="text"*/
}
{/*            value={enteredRepeatPassword}*/
}
{/*            onChange={repeatPasswordChangeHandler}*/
}
{/*        />*/
}
{/*        <input type="button" onClick={equalPasswordCheckHandler} value={"비밀번호확인"}/>*/
}
{/*        /!*<button type="button" onClick={equalPasswordCheckHandler}>비밀번호 확인</button>*!/*/
}
{/*        {nicknameDuplicateCheck && emailDuplicateCheck && passwordEqual &&*/
}
{/*            <button type="submit">Add User</button>}*/
}
{/*    </form>*/
}
{/*    /!*</Card>*!/*/
}
{/*</div>*/
}
