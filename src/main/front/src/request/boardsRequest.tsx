import axios from "axios";
import qs from "qs";

axios.defaults.paramsSerializer = params => {
    return qs.stringify(params);
}


export interface boardsPageParams {
    boardWriterNickname: string | null;
    boardTitle: string | null;
    boardContent: string | null;
    boardCategory: string;
    page: number | null;
    sort: any | null;
    direction: string | null;
};

export interface boardsInfoParams {
    boardWriterNickname: string | null;
    boardTitle: string | null;
    boardContent: string | null;
    boardCategory: string | null;
};

export interface newBoardParams {
    boardCategory: string;
    boardTitle: string | null;
    boardContent: string | null;
}
export const boardsPageRequest = async (boardsPageParams : boardsPageParams) => {
    console.log("request!")
    let params = {
        page : 0,
        sort : "regDate,",
    };
    if (boardsPageParams.page) {params.page = boardsPageParams.page;}
    if (boardsPageParams.direction) {params.sort += boardsPageParams.direction;}
    let url;
    url = "/boards/offset/"
    return axios
        .post(url,{
            boardWriterNickname: boardsPageParams?.boardWriterNickname,
            boardTitle : boardsPageParams?.boardTitle,
            boardContent : boardsPageParams?.boardContent,
            boardCategory : boardsPageParams?.boardCategory
        }, {params})
        .then((res) => res.data)
    // .catch((err) => console.log(err));
}


export const boardsInfoRequest = async (id: string | undefined) => {
    let url;
    url = `/boards/${id}`
    if (id === undefined) {
        return
    }
    const response = await axios.get(url)
    return response.data;
}

export const newBoardRequest = async (params: newBoardParams, token: string) => {
    let url;
    url = "/boards"
    return axios
        .post(url,
            {
                boardCategory: params.boardCategory,
                title: params.boardTitle,
                content: params.boardContent
            }, {
                headers: {
                    authorization: token,
                }
            })
        .then((res) => res.data)
        // .catch((err) => {
        //     console.log(err);
        //     alert(err.response.data.status + err.response.data.code + ' : ' + err.response.data.message)
        //     throw err
        // });
}

export const updateBoardsRequest = async (params: newBoardParams, token: string, boardsId: string) => {
    let url;
    url = `/boards/${boardsId}`
    return axios
        .put(url,
            {
                boardCategory: params.boardCategory,
                title: params.boardTitle,
                content: params.boardContent
            }, {
                headers: {
                    authorization: token,
                }
            })
        .then((res) => res.data)
}

export const deleteBoardsRequest = async (token: string, boardsId: string) => {
    console.log("here")
    console.log(boardsId)
    let url;
    url = `/boards/${boardsId}`
    return axios
        .delete(url,{
                headers: {
                    authorization: token,
                }
            })
        .then((res) => res.data)
}
