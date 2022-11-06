import React, {useCallback, useEffect, useState} from "react";
import axios from "axios";

let logoutTimer;

const AuthContext = React.createContext({
    authorization: "",
    refresh: "",
    isLoggedIn: false,
    id: 0,

    login: (response) => {
    },
    logout: () => {
    },
});

const calculateRemainingTime = (expirationTime) => {
    const currentTime = new Date().getTime();
    const adjExpirationTime = new Date(expirationTime).getTime();
    return adjExpirationTime - currentTime;
};

const retrieveStoredToken = () => {
    const storedAuthorizationToken = localStorage.getItem("authorization");
    const storedRefreshToken = localStorage.getItem("refresh");
    const sortedExpirationDate = localStorage.getItem("expirationTime");
    const remainingTime = calculateRemainingTime(sortedExpirationDate);
    const memberId = localStorage.getItem("id");
    return {
        authorization: storedAuthorizationToken,
        refresh: storedRefreshToken,
        duration: remainingTime,
        memberId: memberId,
    };
};

export const AuthContextProvider = (props) => {
    const tokenData = retrieveStoredToken();
    let authorization;
    let refresh;
    let memberId;
    if (tokenData) {
        authorization = tokenData.authorization;
        refresh = tokenData.refresh
        memberId = tokenData.memberId
    }
    const [authorizationToken, setAuthorizationToken] = useState(authorization);
    const [refreshToken, setRefreshToken] = useState(refresh);
    const [id, setId] = useState(memberId);
    const userIsLogin = !!authorizationToken;

    const logoutHandler = useCallback(() => {
        setAuthorizationToken(null);
        setRefreshToken(null);
        setId(null);
        localStorage.removeItem("authorization");
        localStorage.removeItem("refresh");
        localStorage.removeItem("expirationTime")
        localStorage.removeItem("id");
        localStorage.removeItem("nickname");
        localStorage.removeItem("email");
        //만약 타이머가 있으면 제거한다.
        if (logoutTimer) clearTimeout(logoutTimer);
    }, [setAuthorizationToken]);


    const loginHandler = (response) => {
        setAuthorizationToken(response?.headers?.authorization);
        setRefreshToken(response?.headers?.refresh)
        setId(response?.data?.data?.id)
        const expirationTime = new Date(new Date().getTime() + 29 * 60 * 1000)
        //문자열이어야 한다는 것에 유념한다.
        localStorage.setItem("expirationTime", expirationTime.toISOString())
        localStorage.setItem("authorization", response?.headers?.authorization)
        localStorage.setItem("refresh", response?.headers?.refresh)
        localStorage.setItem("id", response?.data?.data?.id)
        localStorage.setItem("nickname", response?.data?.data?.nickname)
        localStorage.setItem("email", response?.data?.data?.email)
    };


    const extendAuthorizationToken = useCallback(() => {
        const id = localStorage.getItem("id");
        const url = `/tokens/${id}`;
        if (!id) {return}
        axios.get(url, {
            headers: {
                authorization: authorizationToken,
                refresh: refreshToken
            }
        }).then(function (response) {
            if (response.status === 200) {
                const expirationTime = new Date(new Date().getTime() + 29 * 60 * 1000)
                localStorage.setItem("expirationTime", expirationTime.toISOString())
                localStorage.setItem("authorization", response?.headers?.authorization)
                setAuthorizationToken(response?.headers?.authorization)
                console.log("success")
            }
            else {
                console.log(response.data.message);
                logoutHandler();
            }})
                .catch(function (error) {
                console.log(error.response.data.message);
                logoutHandler();
                });
    }, [authorizationToken, logoutHandler, refreshToken])

    useEffect(() => {
        if (tokenData) {
            logoutTimer = setTimeout(extendAuthorizationToken, tokenData.duration);
        }
    }, [tokenData, extendAuthorizationToken]);

    const contextValue = {
        authorization: authorizationToken,
        refresh: refreshToken,
        isLoggedIn: userIsLogin,
        login: loginHandler,
        logout: logoutHandler,
        // id: memberId,
        id: id,
    };

    return (
        <AuthContext.Provider value={contextValue}>
            {props.children}
        </AuthContext.Provider>
    );
};

export default AuthContext;
