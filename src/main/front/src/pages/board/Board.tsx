import classes from "../Board.module.css"
import BoardForm from "../../components/board/BoradForm";
import {useState} from "react";
import {boardsPageParams, boardsPageRequest} from "../../request/boardsRequest";
import React, {Suspense} from "react";
import {boardsPageFetch} from "../../request/test/WrapPromise";
import LoadingSpinners from "../../components/spinner/LoadingSpinner";
import {useQuery} from "react-query";
import {boardsPageRequest2} from "../../request/test/fetchBoards";
import {ErrorBoundary} from "react-error-boundary";
import {UserProfile, UserProfileFallback} from "../../components/TestError";

export const defaultParams = {
    boardWriterNickname: null,
    boardTitle: null,
    boardContent: null,
    boardCategory: "total",
    page: 0,
    sort: "regDate",
    direction: "desc"
}
export default function Board() {
    const [params, setParams] = useState<boardsPageParams>(defaultParams);
    const [refresh, setRefresh] = useState(false);
    const { data } = useQuery([
        "boardsPage",
        params.boardWriterNickname,
        params.boardTitle,
        params.boardContent,
        params.boardCategory,
        params.page,
        params.sort,
        params.direction
    ], () => boardsPageRequest(params), { suspense: true })
    return (
    <ErrorBoundary FallbackComponent={UserProfileFallback}>
        <Suspense fallback={<LoadingSpinners />}>
            <BoardForm
                data={data}
                params={params}
                setParams={setParams}
                refresh={refresh}
                setRefresh={setRefresh}/>
        </Suspense>
    </ErrorBoundary>
    );
}
