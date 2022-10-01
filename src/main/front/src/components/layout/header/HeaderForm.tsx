import classes from "./Header.module.css";
import {Link} from "react-router-dom";
import AuthContext from "../../../store/auth-context";
import {useContext} from "react";

export default function HeaderForm() {
    const authCtx = useContext(AuthContext);

    return (
        <>
            {/*<header className={classes.header}>*/}
            <header className={classes.header}>
                <h1>Web Party</h1>
                <div>자유게시판</div>
                <div>공지사항</div>
                <div>검색</div>
                <div><Link to={`/auth`}>로그인</Link></div>
                {authCtx.isLoggedIn && <div>{localStorage?.getItem("nickname")}</div>}
                <div/>
            </header>
        </>
    )
}