import axios from "axios";

export interface usersUpdateParams {
    email: string;
    nickname: string;
    password: string;
}

export const loginRequest = async (enteredEmail: string, enteredPassword: string) => {
    let url;
    url = "/members/login";
    return await axios.post(url, {
        email: enteredEmail,
        password: enteredPassword
    })
}

export const signUpRequest = async (email: string, nickname: string, password: string) => {
    let url;
    url = "/members"
    return await axios.post(url, {
        email: email,
        nickname: nickname,
        password: password
    })
}

export const nicknameCheckRequest = async (nickname: string) => {
    let url;
    url = "/members/nickname-check"
    return await axios.post(url, {
        nickname: nickname
    })
}

export const emailCheckRequest = async (email: string) => {
    let url;
    url = "/members/email-check"
    return await axios.post(url, {
        email: email
    })
}

export const usersInfoRequest = async (id: string, token: string) => {
    let url;
    url = `/members/${id}`
    const response = await axios.get(url,
        {
            headers: {
                authorization: token,
            }
        })
    return response.data.data;
}



export const updateUsersRequest = async (id : string, usersUpdateParams: usersUpdateParams, token: string) => {
    let url;
    url = `/members/${id}`
    return await axios.put(url, {
        email: usersUpdateParams.email,
        nickname: usersUpdateParams.nickname,
        password: usersUpdateParams.password,
    }, {
        headers: {
            authorization: token,
        }
    })
}
