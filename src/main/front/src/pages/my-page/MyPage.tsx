import MyPageForm from "../../components/my-page/MyPageForm";
import {ErrorBoundary} from "react-error-boundary";
import {UserProfileFallback} from "../../components/TestError";
import React, {Suspense, useContext, useEffect} from "react";
import LoadingSpinners from "../../components/spinner/LoadingSpinner";
import AuthContext from "../../store/context/auth-context";
import {useNavigate} from "react-router-dom";

export default function MyPage() {
    const authCtx = useContext(AuthContext);
    const navigate = useNavigate();

    useEffect(() => {
        if (!authCtx.isLoggedIn) {
            navigate("/home")
        }
    }, [authCtx.isLoggedIn]);

    return (
    <ErrorBoundary FallbackComponent={UserProfileFallback}>
        <Suspense fallback={<LoadingSpinners />}>
            <MyPageForm />
        </Suspense>
    </ErrorBoundary>
    )
}
