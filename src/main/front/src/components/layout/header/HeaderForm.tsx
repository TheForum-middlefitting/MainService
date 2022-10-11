import classes from "./Header.module.css";
import {Link, useNavigate} from "react-router-dom";
import AuthContext from "../../../store/auth-context";
import {useContext, useEffect, useState} from "react";

export default function HeaderForm() {
    const authCtx = useContext(AuthContext);
    const navigate = useNavigate();
    const [isLogin, setIsLogin] = useState<boolean>(!!localStorage.getItem('authorization'));

    useEffect(() => {
        setIsLogin(authCtx.isLoggedIn)
    }, [authCtx.isLoggedIn])

    const toLoginPage = () => {
        navigate('/auth');
    }

    const loginForm = <div><button onClick={toLoginPage}>로그인</button></div>;
    const logoutForm = <div><button onClick={authCtx.logout}>로그아웃</button></div>;

    return (
        <>
            {/*<header className={classes.header}>*/}
            <header className={classes.header}>
                <h1><Link to={'/home'}>Web Party</Link></h1>
                <div>자유게시판</div>
                <div>공지사항</div>
                <div>검색</div>
                {isLogin ? logoutForm : loginForm}
                {authCtx.isLoggedIn && <div>{localStorage?.getItem("nickname")}</div>}
                <div/>
            </header>
        </>
    )
}