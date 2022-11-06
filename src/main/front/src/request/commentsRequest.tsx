import axios from "axios";
import qs from "qs";
axios.defaults.paramsSerializer = params => {
    return qs.stringify(params);
}


export interface commentsParams {
    commentsContent : string | null;
};

export interface newCommentsParams {
    boardsId : string;
    commentsContent : string | null;
};

export interface commentsUpdateParams {
    commentsContent : string | null;
    boardsId : string | null;
    commentsId : string | null;
}


export const commentsPageRequest = async (boardsId : string, commentsId : any) => {
    let url;
    console.log("query")
    console.log(commentsId)
    url = `/boards/${boardsId}/comments/next/`
    const response = await axios.post(url,{
        commentId: commentsId,
    })
    if(response.status !== 200) {
        alert(response.status)
    }
    return response.data;
}

export const newCommentsRequest = async (params : newCommentsParams, token : string) => {
    let url;
    url = `/boards/${params.boardsId}/comments`
    console.log(params)
    const response = await axios.post(url, {
        content: params.commentsContent
    }, {
        headers: {
            authorization: token,
        }
    })
    return response;
}

export const commentsUpdateRequest = async (params : commentsUpdateParams, token : string) => {
    let url;
    url = `/boards/${params.boardsId}/comments/${params.commentsId}`
    return await axios.put(url, {
        content: params.commentsContent
    }, {
        headers: {
            authorization: token,
        }
    }).catch(function (error) {
        alert(error.response.data.message);
        return {'status': error}
    });
}

export const commentsDeleteRequest = async (boardsId : string, commentsId : string | null, token : string) => {
    let url;
    url = `/boards/${boardsId}/comments/${commentsId}`
    return await axios.delete(url, {
        headers: {
            authorization: token,
        }
    }).catch(function (error) {
        alert(error.response.data.message);
        return {'status': error}
    });
}
