import axios from "axios";
import qs from "qs";
import {boardsPageParams} from "../boardsRequest";

axios.defaults.paramsSerializer = params => {
    return qs.stringify(params);
}

export const boardsPageRequest2 = async (boardsPageParams : boardsPageParams) => {
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
