import axios from "axios";
import qs from "qs";
axios.defaults.paramsSerializer = params => {
    return qs.stringify(params);
}


export interface boardsPageParams {
    boardWriterNickname : string | null;
    boardTitle : string | null;
    boardContent : string | null;
    boardCategory : string;
    page : number | null;
    sort : any | null;
    direction : string | null;
};

export const boardsPageRequest = async (boardsPageParams : boardsPageParams) => {
    console.log("here!")
    let params = {
        page : 0,
        sort : "regDate,",
    };
    if (boardsPageParams.page) {params.page = boardsPageParams.page;}
    // if (boardsPageParams.sort) {params.sort = boardsPageParams.sort;}
    if (boardsPageParams.direction) {params.sort += boardsPageParams.direction;}


    let url;
    url = "/boards/offset/"
    const response = await axios.post(url,{
        boardWriterNickname: boardsPageParams?.boardWriterNickname,
        boardTitle : boardsPageParams?.boardTitle,
        boardContent : boardsPageParams?.boardContent,
        boardCategory : boardsPageParams?.boardCategory
    }, {params})
    if(response.status !== 200) {
        alert(response.status)
    }
    return response.data;
}
