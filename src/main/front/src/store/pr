// import React, { useCallback, useEffect, useState } from "react";
//
// let logoutTimer: any
//
// const AuthContext = React.createContext({
//     authorization: "",
//     refresh: "",
//     isLoggedIn: false,
//     login: () => {},
//     logout: () => {},
// });
//
// const calculateRemainingTime = (expirationTime: string) => {
//     const currentTime = new Date().getTime();
//     const adjExpirationTime = new Date(expirationTime).getTime();
//     const remainingDuration = adjExpirationTime - currentTime;
//     return remainingDuration;
// };
//
// const retrieveStoredToken = () => {
//     const storedAuthorizationToken = localStorage.getItem("authorization");
//     const storedRefreshToken = localStorage?.getItem("refresh");
//     const sortedExpirationDate = localStorage.getItem("expirationTime");
//     if(!storedAuthorizationToken || !storedRefreshToken || !sortedExpirationDate) {
//         return null;
//     }
//     const remainingTime = calculateRemainingTime(sortedExpirationDate);
//
//     if (remainingTime <= 60 * 1000) {
//         localStorage.removeItem("authorization");
//         localStorage.removeItem("expirationTime");
//         localStorage.removeItem("refresh");
//         localStorage.removeItem("id");
//         localStorage.removeItem("nickname");
//         localStorage.removeItem("email");
//         return null;
//     }
//     return {
//         authorization: storedAuthorizationToken,
//         refresh: storedRefreshToken,
//         duration: remainingTime,
//     };
// };
//
// export const AuthContextProvider = (props : any) => {
//     const tokenData = retrieveStoredToken();
//     let authorization: any;
//     let refresh: any;
//     if (tokenData) {
//         authorization = tokenData.authorization;
//         refresh = tokenData.authorization
//     }
//     const [authorizationToken, setAuthorizationToken] = useState(authorization);
//     const [refreshToken, setRefreshToken] = useState(refresh);
//
//     const userIsLogin = !!authorizationToken;
//
//     const logoutHandler = useCallback(() => {
//         // @ts-ignore
//         setAuthorizationToken(null);
//         localStorage.removeItem("authorization");
//         localStorage.removeItem("refresh");
//         localStorage.removeItem("expirationTime")
//
//         //만약 타이머가 있으면 제거한다.
//         if (logoutTimer) {
//             clearTimeout(logoutTimer);
//         }
//     }, [setAuthorizationToken]);
//
//     const loginHandler = (authorization: any, refresh: any, expirationTime: any) => {
//         setAuthorizationToken(authorization);
//         localStorage.setItem("authorization", authorization);
//         localStorage.setItem("refresh", refresh);
//         //문자열이어야 한다는 것에 유념한다.
//         localStorage.setItem("expirationTime", expirationTime);
//
//         const remainingTime = calculateRemainingTime(expirationTime);
//         console.log(remainingTime);
//         console.log("hello!");
//         logoutTimer = setTimeout(logoutHandler, remainingTime);
//     };
//
//     useEffect(() => {
//         if (tokenData) {
//             logoutTimer = setTimeout(logoutHandler, tokenData.duration);
//             console.log(tokenData.duration)
//         }
//     }, [tokenData,logoutHandler]);
//
//     const contextValue = {
//         authorization: authorizationToken,
//         refresh: refreshToken,
//         isLoggedIn: userIsLogin,
//         login: loginHandler,
//         logout: logoutHandler,
//     };
//
//     // @ts-ignore
//     return (
//         <AuthContext.Provider value={contextValue}>
//             {props.children}
//         </AuthContext.Provider>
//     );
// };
//
// // @ts-ignore
// export default AuthContext;


import React, { useCallback, useEffect, useState } from "react";
import axios from "axios";

let logoutTimer;

const AuthContext = React.createContext({
    authorization: "",
    refresh: "",
    isLoggedIn: false,
    login: (authorization, refresh, expirationTime) => {},
    logout: () => {},
});

const calculateRemainingTime = (expirationTime) => {
    const currentTime = new Date().getTime();
    const adjExpirationTime = new Date(expirationTime).getTime();
    const remainingDuration = adjExpirationTime - currentTime;
    return remainingDuration;
};

const retrieveStoredToken = () => {
    const storedAuthorizationToken = localStorage.getItem("authorization");
    const storedRefreshToken = localStorage.getItem("refresh");
    const sortedExpirationDate = localStorage.getItem("expirationTime");
    const remainingTime = calculateRemainingTime(sortedExpirationDate);

    // if (remainingTime <= 60 * 1000) {
    //     localStorage.removeItem("authorization");
    //     localStorage.removeItem("expirationTime");
    //     localStorage.removeItem("refresh");
    //     localStorage.removeItem("id");
    //     localStorage.removeItem("nickname");
    //     localStorage.removeItem("email");
    //     return null;
    // }
    return {
        authorization: storedAuthorizationToken,
        refresh: storedRefreshToken,
        duration: remainingTime,
    };
};

export const AuthContextProvider = (props) => {
    const tokenData = retrieveStoredToken();
    let authorization;
    let refresh;
    if (tokenData) {
        authorization = tokenData.authorization;
        refresh = tokenData.refresh
    }
    const [authorizationToken, setAuthorizationToken] = useState(authorization);
    const [refreshToken, setRefreshToken] = useState(refresh);
    const userIsLogin = !!authorizationToken;

    const logoutHandler = useCallback(() => {
        setAuthorizationToken(null);
        localStorage.removeItem("authorization");
        localStorage.removeItem("refresh");
        localStorage.removeItem("expirationTime")
        localStorage.removeItem("id");
        localStorage.removeItem("nickname");
        localStorage.removeItem("email");

        //만약 타이머가 있으면 제거한다.
        if (logoutTimer) {
            clearTimeout(logoutTimer);
        }
    }, [setAuthorizationToken]);

    const loginHandler = (authorization, refresh, expirationTime) => {
        setAuthorizationToken(authorization);
        setRefreshToken(refresh)
        localStorage.setItem("authorization", authorization);
        localStorage.setItem("refresh", refresh);
        //문자열이어야 한다는 것에 유념한다.
        localStorage.setItem("expirationTime", expirationTime);

        const remainingTime = calculateRemainingTime(expirationTime);
        console.log(remainingTime);
        // logoutTimer = setTimeout(logoutHandler, remainingTime);
    };




    const extendAuthorizationToken = useCallback(() => {
        const id = localStorage.getItem("id");
        const url = `/tokens/${id}`;
        axios.get(url, {
            headers : {
                authorization : authorizationToken,
                refresh : refreshToken
            }
        }).then(function (response) {
            if(response.status === 200){
                const expirationTime = new Date(new Date().getTime() + 29 * 60 * 1000)
                localStorage.setItem("expirationTime", expirationTime.toISOString())
                localStorage.setItem("authorization", response?.headers?.authorization)
                setAuthorizationToken(response?.headers?.authorization)
                console.log("success")
            } else {
                alert(response.data.message);
                logoutHandler();
            }
        }).catch(function (error) {
            alert(error.response.data.message);
            console.log("here")
            logoutHandler();
        });
    }, [authorizationToken, logoutHandler, refreshToken])




    useEffect(() => {
        if (tokenData) {
            logoutTimer = setTimeout(extendAuthorizationToken, tokenData.duration);
            console.log(tokenData.duration)
        }
    }, [tokenData,extendAuthorizationToken]);

    const contextValue = {
        authorization: authorizationToken,
        refresh: refreshToken,
        isLoggedIn: userIsLogin,
        login: loginHandler,
        logout: logoutHandler,
    };

    return (
        <AuthContext.Provider value={contextValue}>
            {props.children}
        </AuthContext.Provider>
    );
};

export default AuthContext;
