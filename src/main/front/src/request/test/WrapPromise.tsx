import {fetchUser} from "./fetchUser";
import {boardsPageRequest2} from "./fetchBoards";
import {boardsPageParams, newBoardParams, newBoardRequest} from "../boardsRequest";
import {useState} from "react";
import {useDispatch} from "react-redux";
import {errorActions} from "../../store/redux/errorSlice";
import {Simulate} from "react-dom/test-utils";
import error = Simulate.error;
import {throws} from "assert";

export const dataFetch = () => {
    const userPromise = fetchUser();
    console.log(userPromise)
    return {
        user: wrapPromiseAlertError(userPromise),
    };
};

export const boardsPageFetch = (boardsPageParams: boardsPageParams) => {
    const boardsPromise = boardsPageRequest2(boardsPageParams);
    return {
        board: wrapPromiseAlertError(boardsPromise),
    };
}

export const postBoards = (params: newBoardParams, token: string) => {
    const boardsPromise = newBoardRequest(params, token);
    const message = "게시물 작성을 완료하였습니다!";
    return {
        board: wrapPromiseAlertErrorSuccess(boardsPromise, message),
    };
}

export const wrapPromiseAlertError = (promise: any) => {
    let status = "pending";
    let result: any;
    let suspend = promise.then(
        (res: any) => {
            status = "success";
            result = res;
        },
        (err: any) => {
            status = "error";
            result = err;
            console.log("here")
            alert(err.response.data.status + err.response.data.code + ' : ' + err.response.data.message);
        });
    return {
        read() {
            if (status === "pending") {
                throw suspend;
            } else if (status === "error") {
                return result
            } else if (status === "success") {
                return result;
            }
        },
    };
};


export const wrapPromiseAlertErrorSuccess = (promise: any, message : string) => {
    let status = "pending";
    let result: any;
    let suspend = promise.then(
        (res: any) => {
            status = "success";
            result = res;
            alert(message)
        },
        (err: any) => {
            status = "error";
            result = err;
            console.log("here")
            alert(err.response.data.status + err.response.data.code + ' : ' + err.response.data.message);
        });
    return {
        read() {
            if (status === "pending") {
                throw suspend;
            } else if (status === "error") {
                return result
            } else if (status === "success") {
                return result;
            }
        },
    };
};
