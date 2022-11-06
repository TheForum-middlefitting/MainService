import React, {useCallback, useContext, useEffect, useState} from "react";
import {Link, useLocation, useNavigate} from "react-router-dom";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import {Simulate} from "react-dom/test-utils";
import AuthContext from "../../../store/context/auth-context";
import {
    newBoardParams,
    updateBoardsRequest
} from "../../../request/boardsRequest";
import {postBoards} from "../../../request/test/WrapPromise";
import error = Simulate.error;
import {useMutation, useQuery, useQueryClient} from "react-query";
import {errorActions} from "../../../store/redux/errorSlice";
import {alertActions} from "../../../store/redux/alertSlice";
import {warningActions} from "../../../store/redux/warningSlice";
import {useDispatch} from "react-redux";

export default function BoardInfoUpdate(props : any) {
    const location = useLocation();
    const authCtx = useContext(AuthContext);
    const navigate = useNavigate();
    const [checkFailed, setCheckFailed] = useState(false)
    const [boardTitle, setBoardTitle] = useState(props.boardsInfoData.title);
    const [boardCategory, setBoardCategory] = useState(props.boardsInfoData.boardCategory);
    const [boardContent, setBoardContent] = useState(props.boardsInfoData.content);
    const [isLoading, setIsLoading] = useState(false);
    const dispatch = useDispatch()
    const queryClient = useQueryClient();
    useEffect(() => {
        if (!authCtx.isLoggedIn) {
            navigate('/auth', {
                state: {
                    returnUrl: location.pathname,
                }
            });
        }
    }, [authCtx.isLoggedIn, navigate, location.pathname]);

    const boardTitleChangeHandler = (event: any) => {
        setBoardTitle(event.currentTarget.value);
    };
    const boardCategoryChangeHandler = (event: any) => {
        setBoardCategory(event.currentTarget.value);
    };
    const boardContentChangeHandler = (event: any) => {
        setBoardContent(event.currentTarget.value);
    };

    const boardTitleCheck = () => {
        return boardTitle.trim().length >= 10 && boardTitle.trim().length <= 20
    }
    const boardCategoryCheck = () => {
        return boardCategory === "free" || boardCategory === "notice"
    }
    const boardContentCheck = () => {
        return boardContent.trim().length >= 10 && boardContent.trim().length <= 100
    }
    const updateBoardsMutation = useMutation((params: newBoardParams) => updateBoardsRequest(params, authCtx.authorization, props.boardsInfoData.boardId), {
        onSuccess: () => {
            queryClient.invalidateQueries(["boardsInfo", props.boardsInfoData.boardId.toString()],{ refetchInactive: true });
            setIsLoading(false)
            dispatch(alertActions.setMessage("게시물 수정을 완료하였습니다!"));
            dispatch(alertActions.open());
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

    const handleSubmit = async () => {
        setCheckFailed(false)
        const params: newBoardParams = {
            boardCategory: boardCategory,
            boardTitle: boardTitle,
            boardContent: boardContent
        }
        setIsLoading(true)
        updateBoardsMutation.mutate(params);
    }

    const undoHandler = () => {
        navigate('/board');
    }

    const undoCheck = () => {
        dispatch(warningActions.setMessage("게시물 수정을 취소합니다"));
        dispatch(warningActions.open());
        dispatch(warningActions.setFunction(() => props.setIsUpdating(false)));
    }

    const submitCheck = (event: React.FormEvent) => {
        event.preventDefault()
        if (!boardTitleCheck() || !boardCategoryCheck() || !boardContentCheck()) {
            setCheckFailed(true)
            return
        }
        dispatch(warningActions.setMessage("게시물을 수정하시겠습니까?"));
        dispatch(warningActions.open());
        dispatch(warningActions.setFunction(handleSubmit));
    }

    return (
        <Form noValidate onSubmit={submitCheck}>
            <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                <Form.Label>제목</Form.Label>
                <Form.Control
                    type="text"
                    placeholder="제목을 입력해 주세요"
                    onChange={boardTitleChangeHandler}
                    value={boardTitle}
                    isValid={boardTitleCheck()}
                    isInvalid={!boardTitleCheck() && checkFailed}
                />
                <Form.Control.Feedback>Looks good!</Form.Control.Feedback>
                <Form.Control.Feedback type="invalid">
                    제목은 10 ~ 20 글자 사이로 입력해 주세요!
                </Form.Control.Feedback>
            </Form.Group>
            <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                <Form.Label>카테고리</Form.Label>
                <Form.Select
                    aria-label="Default select example"
                    onChange={boardCategoryChangeHandler}
                    value={boardCategory}
                    isValid={boardCategoryCheck()}
                    isInvalid={!boardCategoryCheck() && checkFailed}
                >
                    <option>카테고리를 선택해 주세요</option>
                    <option value="free">자유게시판</option>
                    <option value="notice">공지사항</option>
                </Form.Select>
                <Form.Control.Feedback>Looks good!</Form.Control.Feedback>
                <Form.Control.Feedback type="invalid">
                    카테고리를 선택해 주세요!
                </Form.Control.Feedback>
            </Form.Group>
            <Form.Group controlId="formFileMultiple" className="mb-3">
                <Form.Label>사진</Form.Label>
                <Form.Control type="file" multiple/>
            </Form.Group>
            <Form.Group className="mb-3" controlId="exampleForm.ControlTextarea1">
                <Form.Label>본문</Form.Label>
                <Form.Control
                    as="textarea"
                    rows={10}
                    placeholder="내용을 입력해 주세요"
                    onChange={boardContentChangeHandler}
                    value={boardContent}
                    isValid={boardContentCheck()}
                    isInvalid={!boardContentCheck() && checkFailed}
                />
                <Form.Control.Feedback>Looks good!</Form.Control.Feedback>
                <Form.Control.Feedback type="invalid">
                    본문은 10 ~ 100 글자 사이로 입력해 주세요!
                </Form.Control.Feedback>
            </Form.Group>
            <Form.Group className="d-flex justify-content-center mb-3" controlId="exampleForm.ControlTextarea1">
                <Button className={"me-3"} variant={"outline-danger"} onClick={undoCheck}
                        disabled={isLoading}>취소</Button>
                <Button className={"me-3"} type="submit" disabled={isLoading} variant={"outline-warning"}>수정</Button>
            </Form.Group>
        </Form>
    )
}

