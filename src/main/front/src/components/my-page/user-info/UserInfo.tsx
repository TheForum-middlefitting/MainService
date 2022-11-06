import React, {useContext, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import AuthContext from "../../../store/context/auth-context";
import Form from "react-bootstrap/Form";
import {Container} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import {errorActions} from "../../../store/redux/errorSlice";
import {alertActions} from "../../../store/redux/alertSlice";
import {warningActions} from "../../../store/redux/warningSlice";
import {useDispatch} from "react-redux";
import {useMutation, useQueryClient} from "react-query";
import {
    emailCheckRequest,
    nicknameCheckRequest,
    signUpRequest,
    updateUsersRequest, usersUpdateParams
} from "../../../request/usersRequesr";

export default function UserInfo(props: any) {
    console.log(props.data)
    const [enteredEmail, setEnteredEmail] = useState(props?.data?.email);
    const [enteredNickname, setEnteredNickname] = useState(props?.data?.nickname);
    const [enteredPassword, setEnteredPassword] = useState("");
    const [emailDuplicateCheck, setEmailDuplicateCheck] = useState(true)
    const [nicknameDuplicateCheck, setNicknameDuplicateCheck] = useState(true)
    const [emailEntered, setEmailEntered] = useState(false);
    const [nicknameEntered, setNicknameEntered] = useState(false);
    const navigate = useNavigate();
    const authCtx = useContext(AuthContext);
    const dispatch = useDispatch();
    const queryClient = useQueryClient();

    useEffect(() => {
        if (!authCtx.isLoggedIn) {
            navigate("/home")
        }
    }, [authCtx.isLoggedIn]);


    const UpdateUsersMutation = useMutation((params: usersUpdateParams) => updateUsersRequest(authCtx.id.toString(), params, authCtx.authorization), {
        onSuccess: (response) => {
            queryClient.invalidateQueries(["usersInfo", authCtx.id],
                { refetchInactive: true })
                .then(r => {
                    setNicknameEntered(false)
                    setEnteredPassword("")
                    dispatch(alertActions.setMessage("회원정보 수정을 완료하였습니다!"));
                    dispatch(alertActions.open());
                })
        },
        onError: (error: any) => {
            dispatch(alertActions.setMessage(error.response.data.message));
            dispatch(alertActions.open());
        },
    });

    const emailCheckMutation = useMutation((params: string) => emailCheckRequest(params), {
        onSuccess: (response) => {
            setEmailDuplicateCheck(true);
        },
        onError: (error: any) => {
            dispatch(alertActions.setMessage(error.response.data.message));
            dispatch(alertActions.open());
        },
    });

    const nicknameCheckMutation = useMutation((params: string) => nicknameCheckRequest(params), {
        onSuccess: (response) => {
            setNicknameDuplicateCheck(true);
        },
        onError: (error: any) => {
            dispatch(alertActions.setMessage(error.response.data.message));
            dispatch(alertActions.open());
        },
    });


    const updateUserHandler = (event: React.FormEvent) => {
        event.preventDefault();
        if (enteredNickname.trim().length === 0 || enteredNickname.trim().length === 0 || !enteredPasswordParseCheck()) {
            dispatch(alertActions.setMessage("비밀번호는 10 ~ 20 자리로 입력하세요!"));
            dispatch(alertActions.open());
            return;
        }
        const params = {
            email: enteredEmail.trim(),
            nickname: enteredNickname.trim(),
            password: enteredPassword.trim(),
        }
        UpdateUsersMutation.mutate(params)
    };

    // useEffect(() => {
    //     setEmailDuplicateCheck((prevState) => enteredEmail === props?.data?.email)
    //     setEmailEntered((prevState) => enteredEmail !== props?.data?.email)
    // }, [enteredEmail, props?.data?.email])

    useEffect(() => {
        setNicknameDuplicateCheck((prevState) => enteredNickname === props?.data?.nickname)
        setNicknameEntered((prevState) => enteredNickname !== props?.data?.nickname)
    }, [enteredNickname, props?.data?.nickname])


    // const emailChangeHandler = (event: any) => {
    //     setEnteredEmail(event.currentTarget.value);
    // };

    const nicknameChangeHandler = (event: any) => {
        setEnteredNickname(event.currentTarget.value);
    };

    const passwordChangeHandler = (event: any) => {
        setEnteredPassword(event.currentTarget.value);
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

    const enteredPasswordParseCheck = () => {
        return !(enteredPassword.trim().length < 10 || enteredPassword.trim().length > 20);
    }

    return (
        <>
            <Container
                className={"mx-auto my-3 container-sm"}
                style={{maxWidth: "540px"}}
            >
                <br/>
                <h2 className={"text-center"}>
                    <strong>회원정보</strong>
                </h2>
                <Form onSubmit={updateUserHandler}>
                    <Form.Group className="mb-3">
                        <Form.Label><strong>아이디</strong></Form.Label>
                        <Form.Control
                            placeholder="이메일을 입력해 주세요"
                            id="email"
                            type="email"
                            required={true}
                            value={enteredEmail}
                            disabled={true}
                            // onChange={emailChangeHandler}
                            // isValid={emailEntered && emailParseCheckHandler()}
                            // isInvalid={emailEntered && !emailParseCheckHandler()}
                        />
                    {/*    <Form.Control.Feedback>올바른 형식입니다</Form.Control.Feedback>*/}
                    {/*    <Form.Control.Feedback type="invalid">*/}
                    {/*        올바른 이메일 형식을 입력해 주세요!*/}
                    {/*    </Form.Control.Feedback>*/}
                    </Form.Group>
                    {/*<div className="d-grid mb-3">*/}
                    {/*    <Button type="button" variant={"outline-primary"} onClick={emailDuplicateCheckHandler}*/}
                    {/*            disabled={emailDuplicateCheck}>*/}
                    {/*        {props?.data?.email === enteredEmail ? "이메일을 변경해 보세요" : (emailDuplicateCheck ? "사용 가능한 이메일입니다!" : "이메일 중복 체크")}*/}
                    {/*    </Button>*/}
                    {/*</div>*/}

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
                            {props?.data?.nickname === enteredNickname ? "닉네임을 변경해 보세요" : (nicknameDuplicateCheck ? "사용 가능한 닉네임입니다!" : "닉네임 중복 체크")}
                        </Button>
                    </div>

                    <Form.Group className="mb-3">
                        <Form.Label><strong>패스워드</strong></Form.Label>
                        <Form.Control
                            placeholder="비밀번호를 입력해 주세요"
                            id="password"
                            type="password"
                            required={true}
                            value={enteredPassword}
                            onChange={passwordChangeHandler}
                        />
                    </Form.Group>

                    <div className="d-grid mb-3">
                        <Button
                            type="submit"
                            disabled={!(nicknameDuplicateCheck && emailDuplicateCheck) || (enteredEmail === props?.data?.email && enteredNickname === props?.data?.nickname)}>
                            회원정보 수정
                        </Button>
                    </div>
                </Form>
            </Container>
        </>
    );
}
