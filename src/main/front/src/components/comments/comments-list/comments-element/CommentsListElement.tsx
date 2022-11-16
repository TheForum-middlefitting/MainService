import Badge from "react-bootstrap/Badge";
import React, {useContext, useState} from "react";
import Button from "react-bootstrap/Button";
import AuthContext from "../../../../store/context/auth-context";
import Form from "react-bootstrap/Form";
import InputGroup from "react-bootstrap/InputGroup";
import {useDispatch, useSelector} from "react-redux";
import {warningActions} from "../../../../store/redux/warningSlice";
import {commentsDeleteRequest, commentsUpdateParams, commentsUpdateRequest} from "../../../../request/commentsRequest";
import {useMutation, useQueryClient} from "react-query";
import {newBoardParams, newBoardRequest} from "../../../../request/boardsRequest";
import {alertActions} from "../../../../store/redux/alertSlice";
import {errorActions} from "../../../../store/redux/errorSlice";
import {elapsedTime} from "../../../utils/dateParse";

export default function CommentsListElement(props: any) {
    const authCtx = useContext(AuthContext);
    const [updating, setUpdating] = useState(false);
    const [content, setContent] = useState(props.content);
    const [isLoading, setIsLoading] = useState(false);
    const dispatch = useDispatch();
    const queryClient = useQueryClient();
    const elapsedDate = elapsedTime(props.regDate);


    const updateCommentsMutation = useMutation((params: commentsUpdateParams) => commentsUpdateRequest(params, authCtx.authorization), {
        onSuccess: () => {
            queryClient.invalidateQueries(["boardsCommentsPage" + props.boardsId],
                { refetchInactive: true })
                .then(r => {
                    setIsLoading(false)
                    setUpdating(!updating);
            })
            dispatch(alertActions.setMessage("댓글 수정을 완료하였습니다!"));
            dispatch(alertActions.open());
        },
        onError: (error : any) => {
            dispatch(errorActions.setStatus(error.response.data.status));
            dispatch(errorActions.setMessage(error.response.data.message));
            dispatch(errorActions.setCode(error.response.data.code));
            dispatch(errorActions.open());
        },
        onSettled: () => {
        }
    });

    const deleteCommentsMutation = useMutation((params) => commentsDeleteRequest(props.boardsId, props.id, authCtx.authorization), {
        onSuccess: () => {
            queryClient.invalidateQueries(["boardsCommentsPage" + props.boardsId],
                { refetchInactive: true })
                .then(r => {
                    setIsLoading(false)
                    props.setCommentsData([])
                    props.setCommentsId(null)
                    props.setRefresh(!props.refresh)
                })
            dispatch(alertActions.setMessage("댓글 삭제를 완료하였습니다!"));
            dispatch(alertActions.open());
        },
        onError: (error : any) => {
            dispatch(errorActions.setStatus(error.response.data.status));
            dispatch(errorActions.setMessage(error.response.data.message));
            dispatch(errorActions.setCode(error.response.data.code));
            dispatch(errorActions.open());
        },
        onSettled: () => {
        }
    });

    const updateCommentsRequestHandler = async () => {
        if (content.trim().length < 10) {
            dispatch(alertActions.setMessage("댓글은 10글자 이상 입력해주세요!"));
            dispatch(alertActions.open());
            return;
        }
        const params : commentsUpdateParams = {
            commentsContent : content.trim(),
            boardsId : props.boardsId,
            commentsId : props.id
        }
        setIsLoading(true)
        updateCommentsMutation.mutate(params);
        // const response = await commentsUpdateRequest(params, authCtx.authorization);
        // if (response.status == 200) {
        //     alert("업데이트 성공!")
            // await queryClient.invalidateQueries(["boardsCommentsPage", props.boardsId, null])
            // props.setCommentsData([])
            // props.setCommentsId(null)
            // props.setRefresh(!props.refresh)
            // setUpdating(!updating);
            // console.log(content)
            // updateStateHandler()
        // }
    };

    const deleteCommentsRequestHandler = async () => {
        // const response = await commentsDeleteRequest(props.boardsId, props.id, authCtx.authorization);
        // if (response.status == 200) {
        //     alert("삭제 성공!")
        //     await queryClient.invalidateQueries(["boardsCommentsPage", null])
        //     props.setCommentsData([])
        //     props.setCommentsId(null)
        //     props.setRefresh(!props.refresh)
        // }
        setIsLoading(true)
        deleteCommentsMutation.mutate();
    };

    const updateStateHandler = () => {
        setUpdating(!updating);
        setContent(props.content);
    }

    const contentChangeHandler = (event : any) => {
        setContent(event.currentTarget.value);
    };

    const modalUpdateStartHandler = () => {
        dispatch(warningActions.setMessage("댓글을 수정하시겠습니까?"));
        dispatch(warningActions.setFunction(updateStateHandler));
        dispatch(warningActions.open());
    };

    const modalUpdateStopHandler = () => {
        dispatch(warningActions.setMessage("정말로 취소하시겠습니까?"));
        dispatch(warningActions.setFunction(updateStateHandler));
        dispatch(warningActions.open());
    };

    const modalUpdateCompleteHandler = () => {
        dispatch(warningActions.setMessage("댓글을 수정합니다"));
        dispatch(warningActions.setFunction(updateCommentsRequestHandler));
        dispatch(warningActions.open());
    };

    const modalDeleteHandler = () => {
        dispatch(warningActions.setMessage("댓글을 삭제하시겠습니까?"));
        dispatch(warningActions.setFunction(deleteCommentsRequestHandler));
        dispatch(warningActions.open());
    };

    const buttonLayout = () => {
        if (authCtx.id == props.memberId) {
            if (isLoading) {
                return (
                    <div>
                        <Badge bg="warning">수정</Badge>
                        <Badge className={"m-1"} bg="danger">삭제</Badge>
                    </div>
                )
            }
            return (
                <div>
                    <Badge style={{cursor: "pointer"}} bg="warning" onClick={modalUpdateStartHandler}>수정</Badge>
                    <Badge style={{cursor: "pointer"}} className={"m-1"} bg="danger" onClick={modalDeleteHandler}>삭제</Badge>
                </div>
            )
        }
    }

    const commentLayout = (
        <div className="list-group-item align-items-start">
            <div className="d-flex w-100 justify-content-between">
                <h6 className="mb-3"><strong>{props.nickname}</strong></h6>
                <p><small>{elapsedDate}</small></p>
            </div>
            <div className="d-flex w-100 justify-content-between">
                <p>{content}</p>
                <p><small>추천수: 6</small></p>
            </div>
            {buttonLayout()}
        </div>
    )

    const updateLayout = (
        <div className="list-group-item align-items-start">
            <div className="d-flex w-100 justify-content-between">
                <h6 className="mb-3"><strong>{props.nickname}</strong></h6>
                <p><small>{elapsedDate}</small></p>
            </div>
            <div>
                <Form role={"submit"}>
                    <InputGroup className="mb-3">
                        <Form.Control as="textarea" value={content} rows={2} onChange={contentChangeHandler}/>
                        <Button variant="outline-secondary" onClick={modalUpdateCompleteHandler} disabled={isLoading}>수정하기</Button>
                    </InputGroup>
                </Form>
            </div>
            <div>
                <Badge style={{cursor: "pointer"}} bg="danger" onClick={modalUpdateStopHandler}>취소</Badge>
            </div>
        </div>
    )


    return (
        <>
            {updating ? updateLayout : commentLayout}
        </>
    )
}
