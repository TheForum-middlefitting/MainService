import UserInfo from "./user-info/UserInfo";
import {useQuery} from "react-query";
import AuthContext from "../../store/context/auth-context";
import {useContext, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {usersInfoRequest} from "../../request/usersRequesr";

export default function MyPageForm(){
    const authCtx = useContext(AuthContext);
    const {data} = useQuery(["usersInfo", authCtx.id], () => usersInfoRequest(authCtx.id.toString(), authCtx.authorization), {suspense: true})

    return (
        <>
            {<UserInfo data={data}/>}
        </>
    )
}
