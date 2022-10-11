import classes from "./LoginForm.module.css"
import React, {useContext, useEffect, useRef, useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import axios from "axios";
import AuthContext from "../../store/auth-context";


export default function LoginForm() {
    const [isLogin, setIsLogin] = useState<boolean>(!!localStorage.getItem('authorization'));
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const emailInputRef = useRef<HTMLInputElement>(null)
    const passwordInputRef = useRef<HTMLInputElement>(null)
    const authCtx = useContext(AuthContext)
    const navigate = useNavigate();

    useEffect(() => {
        if (isLogin) {
            navigate(-1)
        }
    }, [isLogin]);


    const submitHandler = (event: React.FormEvent): void => {
        event.preventDefault();
        const enteredEmail = emailInputRef?.current?.value;
        const enteredPassword = passwordInputRef?.current?.value;

        let url;
        url = "/members/login";
        axios.post(url, {
            email: enteredEmail,
            password: enteredPassword
        }).then(function (response) {
            if(response.status === 200){
                authCtx.login(response)
                setIsLogin(true)
            } else {
                alert(response.data.message);
            }
        }).catch(function (error) {
            alert(error.response.data.message);
        });
        setIsLoading(false)
    }


    return (
        <section className={classes.auth}>
            <h1>{isLogin ? "Login" : "Sign Up"}</h1>
            <form onSubmit={submitHandler}>
                <div className={classes.control}>
                    <label htmlFor="email">Your Email</label>
                    <input
                        type="email"
                        id="email"
                        required
                        ref={emailInputRef}/>
                </div>
                <div className={classes.control}>
                    <label htmlFor="password">Your Password</label>
                    <input
                        type="password"
                        id="password"
                        required
                        ref={passwordInputRef}
                    />
                </div>
                <div className={classes.actions}>
                    {!isLoading && <button>Login</button>}
                    {isLoading && <p>Sending Request..</p>}
                </div>
            </form>
            <p/>
            {!isLoading && <Link to={`/signup`}>Create new account</Link>}
        </section>

    )
}