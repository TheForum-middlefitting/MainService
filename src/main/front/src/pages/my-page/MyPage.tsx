import MyPageForm from "../../components/my-page/MyPageForm";
import {ErrorBoundary} from "react-error-boundary";
import {UserProfileFallback} from "../../components/TestError";
import React, {Suspense, useContext, useEffect, useState} from "react";
import LoadingSpinners from "../../components/spinner/LoadingSpinner";
import AuthContext from "../../store/context/auth-context";
import {useNavigate} from "react-router-dom";
import {Nav} from "react-bootstrap";
import UsersDeleteForm from "../../components/my-page/user-delete/UsersDeleteForm";

export default function MyPage() {
    const authCtx = useContext(AuthContext);
    const navigate = useNavigate();
    type myPage = "users-info" | "delete-users"
    const [selected, setSelected] = useState<myPage>("users-info")

    useEffect(() => {
        if (!authCtx.isLoggedIn) {
            navigate("/home")
        }
    }, [authCtx.isLoggedIn]);

    const selectedHandler = (eventKey : any) => {
        setSelected(eventKey);
    }

    const navBar = (
        <Nav justify variant="tabs" activeKey={selected} onSelect={selectedHandler}>
            <Nav.Item>
                <Nav.Link eventKey="users-info">회원정보</Nav.Link>
            </Nav.Item>
            <Nav.Item>
                <Nav.Link eventKey="delete-users">회원탈퇴</Nav.Link>
            </Nav.Item>
        </Nav>
    )

    return (
    <ErrorBoundary FallbackComponent={UserProfileFallback}>
        <Suspense fallback={<LoadingSpinners />}>
            {navBar}
            {selected === "users-info" && <MyPageForm />}
            {selected === "delete-users" && <UsersDeleteForm />}
        </Suspense>
    </ErrorBoundary>
    )
}
