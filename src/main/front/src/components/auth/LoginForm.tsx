import classes from "./LoginForm.module.css"
import React, {useContext, useEffect, useRef, useState} from "react";
import {Link, useLocation, useNavigate, useParams} from "react-router-dom";
import axios from "axios";
import AuthContext from "../../store/context/auth-context";
import Form from "react-bootstrap/Form";
import Container from "react-bootstrap/Container";
import Button from "react-bootstrap/Button";
import {loginRequest} from "../../request/usersRequesr";
import {useMutation} from "react-query";
import {errorActions} from "../../store/redux/errorSlice";
import {alertActions} from "../../store/redux/alertSlice";
import {warningActions} from "../../store/redux/warningSlice";
import {useDispatch} from "react-redux";


export default function LoginForm() {
    const location = useLocation();
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const emailInputRef = useRef<HTMLInputElement>(null)
    const passwordInputRef = useRef<HTMLInputElement>(null)
    const authCtx = useContext(AuthContext)
    const navigate = useNavigate();
    const dispatch = useDispatch()


    useEffect(() => {
        let returnUrl = location.state.returnUrl
        console.log(location.state.returnUrl)
        if (location.state.returnUrl === '/auth') {
            returnUrl = "/home"
        }
        if (authCtx.isLoggedIn) {
            console.log(location.state)
            navigate(returnUrl)
        }
    }, [authCtx.isLoggedIn, navigate, location.state.returnUrl]);

    const loginMutation = useMutation((params : any ) => loginRequest(params.enteredEmail, params.enteredPassword), {
        onSuccess: (response) => {
            // queryClient.invalidateQueries('persons');
            setIsLoading(false)
            authCtx.login(response)
            dispatch(alertActions.setMessage("로그인을 완료하였습니다!"));
            dispatch(alertActions.open());
        },
        onError: (error : any) => {
            dispatch(alertActions.setMessage(error.response.data.message));
            dispatch(alertActions.open());
        },
        onSettled: () => {
            setIsLoading(false)
        }
    });

    const submitHandler = (event: React.FormEvent): void => {
        event.preventDefault();
        const enteredEmail = emailInputRef?.current?.value;
        const enteredPassword = passwordInputRef?.current?.value;
        const params = {
            enteredEmail,
            enteredPassword
        }
        setIsLoading(true)
        loginMutation.mutate(params);
    }


    return (
        <>
            <Container
                className={"mx-auto my-3 container-sm"}
                style={{maxWidth: "540px"}}
            >
                <br/>
                <h2 className={"text-center"}>
                    <strong>환영합니다!</strong>
                </h2>
                <h5>
                    <div className={"m-3 text-center"}>
                        <p className="text-break">
                            middleFitting 의 게시판입니다
                        </p>
                    </div>
                </h5>
                <hr style={{
                    height: "2px",
                    color: "black",
                    background: "black"
                }}/>
                <h5 className={"text-center"}><strong>아이디로 로그인</strong></h5>
                <Form onSubmit={submitHandler} role={"search"}>
                    <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                        <Form.Label><strong>아이디</strong></Form.Label>
                        <Form.Control
                            type="email"
                            placeholder="아이디"
                            required={true}
                            ref={emailInputRef}
                        />
                    </Form.Group>
                    <Form.Group className="mb-3" controlId="exampleForm.ControlInput2">
                        <Form.Label><strong>비밀번호</strong></Form.Label>
                        <Form.Control
                            type="password"
                            placeholder="비밀번호"
                            required={true}
                            ref={passwordInputRef}
                        />
                    </Form.Group>
                    <div className="d-grid mb-3">
                        <Button type={"submit"} className={"btn-primary"} disabled={isLoading}>로그인</Button>
                    </div>
                </Form>
                <div className={"text-center mb-3 "}>
                    <p>아직 회원이 아니신가요?
                        {!isLoading ? <Link className={"m-2"} to={`/sign-up`}>회원가입</Link> : "  회원가입"}
                    </p>
                    <hr/>
                    <h5 className={"text-center"}><strong>SNS 로그인</strong></h5>
                </div>
            </Container>
        </>
    )
}


{/*<section className={classes.auth}>*/
}
{/*    <h1>{authCtx.isLoggedIn ? "Login" : "Sign Up"}</h1>*/
}
{/*    <form onSubmit={submitHandler}>*/
}
{/*        <div className={classes.control}>*/
}
{/*            <label htmlFor="email">Your Email</label>*/
}
{/*            <input*/
}
{/*                type="email"*/
}
{/*                id="email"*/
}
{/*                required*/
}
{/*                ref={emailInputRef}/>*/
}
{/*        </div>*/
}
{/*        <div className={classes.control}>*/
}
{/*            <label htmlFor="password">Your Password</label>*/
}
{/*            <input*/
}
{/*                type="password"*/
}
{/*                id="password"*/
}
{/*                required*/
}
{/*                ref={passwordInputRef}*/
}
{/*            />*/
}
{/*        </div>*/
}
{/*        <div className={classes.actions}>*/
}
{/*            {!isLoading && <button>Login</button>}*/
}
{/*            {isLoading && <p>Sending Request..</p>}*/
}
{/*        </div>*/
}
{/*    </form>*/
}
{/*    <p/>*/
}
{/*    {!isLoading && <Link to={`/sign-up`}>Create new account</Link>}*/
}
{/*</section>*/
}
