import {useLocation, useNavigate, useParams} from "react-router-dom";
import {Container, Placeholder} from "react-bootstrap";
import Form from "react-bootstrap/Form";
import InputGroup from "react-bootstrap/InputGroup";
import DropdownButton from "react-bootstrap/DropdownButton";
import Dropdown from "react-bootstrap/Dropdown";
import Button from "react-bootstrap/Button";
import React, {Suspense, useContext, useEffect, useState} from "react";
import {boardsPageParams, boardsPageRequest} from "../../request/boardsRequest";
import {boardsInfoParams, boardsInfoRequest} from "../../request/boardsRequest";
import ListGroup from "react-bootstrap/ListGroup";
import BoardBottom from "../board/board-bottom/BoardBottom";
import CommentsList from "../comments/comments-list/CommentsList";
import NewComments from "../comments/new-comments/NewComments";
import {useQuery} from "react-query";
import {ErrorBoundary} from "react-error-boundary";
import {UserProfileFallback} from "../TestError";
import LoadingSpinners from "../spinner/LoadingSpinner";
import Badge from "react-bootstrap/Badge";
import AuthContext from "../../store/context/auth-context";
import {warningActions} from "../../store/redux/warningSlice";
import {useDispatch} from "react-redux";
import BoardInfoContent from "./boardI-info-content/BoardInfoContent";
import BoardInfoUpdate from "./board-info-update/BoardInfoUpdate";

export default function BoardInfoForm(props: any) {
    // const location = useLocation();
    // const [refresh, setRefresh] = useState(false);
    const navigate = useNavigate();
    const params = useParams();
    const {data} = useQuery(["boardsInfo", params?.id?.toString()], () => boardsInfoRequest(params?.id), {suspense: true})
    const boardsInfoData = data.data;
    const [isLoading, setIsLoading] = useState(false);
    const authCtx = useContext(AuthContext)
    const [isUpdating, setIsUpdating] = useState(false);
    const dispatch = useDispatch();

    // let boardsInfoData: boardsInfoParams = {
    //     boardWriterNickname: null,
    //     boardTitle: null,
    //     boardContent: null,
    //     boardCategory: null,
    // };
    // const [boardsInfoData, setBoardsInfoData] = useState<boardsInfoParams>({
    //     boardWriterNickname: null,
    //     boardTitle: null,
    //     boardContent: null,
    //     boardCategory: null,
    // });


    // useEffect(() => {
    //     const getBoards = async () => {
    //         // const response = await boardsInfoRequest(params?.id);
    //         const response = data
    //         if (response.status !==200) {
    //              navigate("/home")
    //         }
    //         const responseData = await response.data;
    //         let loadedBoards: boardsInfoParams = {
    //             boardContent: null,
    //             boardWriterNickname: null,
    //             boardTitle: null,
    //             boardCategory: null,
    //         };
    //         if (response.status === 200) {
    //             loadedBoards = {
    //                 boardWriterNickname: responseData.nickname,
    //                 boardTitle: responseData.title,
    //                 boardContent: responseData.content,
    //                 boardCategory: responseData.boardCategory,
    //             }
    //             setBoardsInfoData(loadedBoards);
    //         }
    //     }
    //     getBoards();
    // }, [refresh])

    const categoryParse = (category: string | null) => {
        if (category === "free") {
            return "자유게시판"
        }
        if (category === "notice") {
            return "공지사항"
        }
        if (category === "total") {
            return "전체게시판"
        }
    }


    //
    // const modalUpdateStartHandler = () => {
    //     dispatch(warningActions.setMessage("게시물을 수정하시겠습니까?"));
    //     dispatch(warningActions.setFunction(() => setIsUpdating(!isUpdating)));
    //     dispatch(warningActions.open());
    // }
    //
    // const modalDeleteHandler = () => {
    //     dispatch(warningActions.setMessage("게시물을 삭제하시겠습니까?"));
    //     dispatch(warningActions.setFunction(() => setIsUpdating(!isUpdating)));
    //     dispatch(warningActions.open());
    // }
    //
    // const buttonLayout = (
    //     <div>
    //         <div className={"d-grid mb-3"}>
    //             <Button disabled={isLoading} variant="outline-warning"
    //                     onClick={modalUpdateStartHandler}>게시물 수정하기</Button>
    //         </div>
    //         <div className={"d-grid mb-3"}>
    //             <Button disabled={isLoading} variant="outline-danger"
    //                     onClick={modalDeleteHandler}>게시물 삭제하기</Button>
    //         </div>
    //     </div>
    // )

    return (
        <>
            {/*<br/>*/}
            {/*<h1><strong>{categoryParse(boardsInfoData.boardCategory)}</strong></h1>*/}
            {/*<hr style={{*/}
            {/*    height: "2px",*/}
            {/*    color: "black",*/}
            {/*    background: "black"*/}
            {/*}}/>*/}
            {/*<div className="d-flex w-200 justify-content-between">*/}
            {/*    <div>*/}
            {/*        <a style={{cursor: "pointer", textDecoration: "none", color: "black"}} className="me-2">사진</a>*/}
            {/*        <a style={{cursor: "pointer", textDecoration: "none", color: "black"}}*/}
            {/*           className="me-2">{boardsInfoData.nickname}</a>*/}
            {/*        <div className="vr mx-2"/>*/}
            {/*        <a style={{cursor: "pointer"}}>시간</a>*/}
            {/*    </div>*/}
            {/*    <div>*/}
            {/*        조회 :*/}
            {/*        <div className="vr mx-2"/>*/}
            {/*        댓글 :*/}
            {/*    </div>*/}
            {/*</div>*/}
            {/*<hr/>*/}
            {/*<h2 className="text-break mb-5">{boardsInfoData.title}</h2>*/}
            {/*<p className="text-break">{boardsInfoData.content}</p>*/}
            {/*<br/><br/><br/><br/><br/><br/>*/}
            {/*{authCtx.id == boardsInfoData.memberId && buttonLayout}*/}
            {/*<hr/>*/}
            {
                !isUpdating ?
                    <BoardInfoContent
                    boardsInfoData={boardsInfoData}
                    setIsUpdating={setIsUpdating}/>
                    :
                    <BoardInfoUpdate
                        boardsInfoData={boardsInfoData}
                        setIsUpdating={setIsUpdating}/>
            }



            {/*<a style={{cursor: "pointer", textDecoration: "none", color: "black"}} className="me-2">사진</a>*/}
            {/*<a style={{cursor: "pointer", textDecoration: "none", color: "black"}}*/}
            {/*   className="me-2">{boardsInfoData.nickname}</a>*/}
            {/*<hr/>*/}
            <ErrorBoundary FallbackComponent={UserProfileFallback}>
                <Suspense fallback={<LoadingSpinners/>}>
                    <CommentsList boardsId={params?.id}/>
                </Suspense>
            </ErrorBoundary>
        </>
    )
}
