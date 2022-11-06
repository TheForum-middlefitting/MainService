import React, {useContext, useState} from "react";
import Button from "react-bootstrap/Button";
import {warningActions} from "../../../store/redux/warningSlice";
import {useDispatch} from "react-redux";
import AuthContext from "../../../store/context/auth-context";
import {useMutation, useQueryClient} from "react-query";
import {deleteBoardsRequest, newBoardParams, updateBoardsRequest} from "../../../request/boardsRequest";
import {alertActions} from "../../../store/redux/alertSlice";
import {errorActions} from "../../../store/redux/errorSlice";
import {useNavigate} from "react-router-dom";

export default function BoardInfoContent(props: any) {
    const authCtx = useContext(AuthContext)
    const dispatch = useDispatch();
    const [isLoading, setIsLoading] = useState(false);
    const queryClient = useQueryClient();
    const navigate = useNavigate();

    const deleteBoardsMutation = useMutation(() => deleteBoardsRequest(authCtx.authorization, props.boardsInfoData.boardId), {
        onSuccess: () => {
            queryClient.invalidateQueries(["boardsInfo", props.boardsInfoData.boardId],{ refetchInactive: true });
            setIsLoading(false)
            dispatch(alertActions.setMessage("게시물 삭제를 완료하였습니다!"));
            dispatch(alertActions.open());
            navigate("/board")
        },
        onError: (error : any) => {
            dispatch(errorActions.setStatus(error.response.data.status));
            dispatch(errorActions.setMessage(error.response.data.message));
            dispatch(errorActions.setCode(error.response.data.code));
            dispatch(errorActions.open());
        },
        onSettled: () => {
            // undoHandler();
            props.setIsUpdating(false)
        }
    });

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

    const deleteBoardsHandler = () => {
        setIsLoading(true);
        deleteBoardsMutation.mutate()
    }

    const modalUpdateStartHandler = () => {
        dispatch(warningActions.setMessage("게시물을 수정하시겠습니까?"));
        dispatch(warningActions.setFunction(() => props.setIsUpdating(true)));
        dispatch(warningActions.open());
    }

    const modalDeleteHandler = () => {
        dispatch(warningActions.setMessage("게시물을 삭제하시겠습니까?"));
        dispatch(warningActions.setFunction(deleteBoardsHandler));
        dispatch(warningActions.open());
    }

    const buttonLayout = (
        <div className={"d-flex justify-content-center mb-3"}>
            <Button className={"me-3"} disabled={isLoading} variant="outline-warning"
                    onClick={modalUpdateStartHandler}>수정</Button>
            <Button className={"me-3"} disabled={isLoading} variant="outline-danger"
                    onClick={modalDeleteHandler}>삭제</Button>
        </div>
    )


    return (
        <>
            <br/>
            <h1><strong>{categoryParse(props.boardsInfoData.boardCategory)}</strong></h1>
            <hr style={{
                height: "2px",
                color: "black",
                background: "black"
            }}/>
            <div className="d-flex w-200 justify-content-between">
                <div>
                    <a style={{cursor: "pointer", textDecoration: "none", color: "black"}} className="me-2">사진</a>
                    <a style={{cursor: "pointer", textDecoration: "none", color: "black"}}
                       className="me-2">{props.boardsInfoData.nickname}</a>
                    <div className="vr mx-2"/>
                    <a style={{cursor: "pointer"}}>시간</a>
                </div>
                <div>
                    조회 :
                    <div className="vr mx-2"/>
                    댓글 :
                </div>
            </div>
            <hr/>
            <h2 className="text-break mb-5">{props.boardsInfoData.title}</h2>
            <p className="text-break">{props.boardsInfoData.content}</p>
            <br/><br/><br/><br/><br/><br/>
            {authCtx.id == props.boardsInfoData.memberId && buttonLayout}
            <hr/>
        </>
    )
}
