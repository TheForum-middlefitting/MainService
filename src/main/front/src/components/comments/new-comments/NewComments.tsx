import Form from "react-bootstrap/Form";
import InputGroup from "react-bootstrap/InputGroup";
import Button from "react-bootstrap/Button";
import React, {useContext, useState} from "react";
import {
    newCommentsParams,
    newCommentsRequest
} from "../../../request/commentsRequest";
import AuthContext from "../../../store/context/auth-context";
import {useMutation, useQueryClient} from "react-query";
import {alertActions} from "../../../store/redux/alertSlice";
import {errorActions} from "../../../store/redux/errorSlice";
import {useDispatch} from "react-redux";
import {warningActions} from "../../../store/redux/warningSlice";
import {Link} from "react-router-dom";

export default function NewComments(props: any) {
    const authCtx = useContext(AuthContext);
    const [content, setContent] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const dispatch = useDispatch();
    const queryClient = useQueryClient();

    const postCommentsMutation = useMutation((params: newCommentsParams) => newCommentsRequest(params, authCtx.authorization), {
        onSuccess: () => {
            queryClient.invalidateQueries(["boardsCommentsPage" + props.boardsId],
                {refetchInactive: true})
                .then(r => {
                    setIsLoading(false)
                    setContent('');
                    props.setRefresh(!props.refresh)
                    props.setCommentsData([])
                    props.setCommentsId(null)
                })
            dispatch(alertActions.setMessage("댓글 작성을 완료하였습니다!"));
            dispatch(alertActions.open());
        },
        onError: (error: any) => {
            dispatch(errorActions.setStatus(error.response.data.status));
            dispatch(errorActions.setMessage(error.response.data.message));
            dispatch(errorActions.setCode(error.response.data.code));
            dispatch(errorActions.open());
        },
        onSettled: () => {
        }
    });

    const commentsContentChangeHandler = (event: any) => {
        setContent(event.currentTarget.value);
    };

    const contentCheckHandler = () => {
        if (content.trim().length < 10) {
            dispatch(alertActions.setMessage("댓글은 10글자 이상 입력해주세요!"));
            dispatch(alertActions.open());
            return false
        }
        return true
    }

    const postCommentHandler = async () => {
        const params: newCommentsParams = {
            boardsId: props.boardsId,
            commentsContent: content.trim(),
        }
        if (!contentCheckHandler()) {
            return;
        }
        setIsLoading(true)
        postCommentsMutation.mutate(params);
        // const response = await newCommentsRequest(params, authCtx.authorization);
        // if (response?.status === 200) {
        //     alert("댓글 작성을 완료하였습니다!!")
        //     setContent('');
        //     props.setRefresh(!props.refresh)
        //     props.setCommentsData([])
        //     props.setCommentsId(null)
        // }
    }

    const submitCheck = (event: React.FormEvent) => {
        event.preventDefault()
        dispatch(warningActions.setMessage("댓글을 작성하시겠습니까?"));
        dispatch(warningActions.open());
        dispatch(warningActions.setFunction(postCommentHandler));
    }

    return (<>
            {/*<Link className={"mb-3"} to={`/sign-up`}>로그인 하고 댓글쓰기</Link>*/}
            <Form noValidate onSubmit={submitCheck}>
                <InputGroup className="mb-5">
                    <Form.Control
                        placeholder={authCtx.isLoggedIn ? "댓글을 입력해 주세요" : "로그인 후 이용 가능합니다"}
                        as="textarea"
                        value={content}
                        onChange={commentsContentChangeHandler}
                        rows={3}
                        disabled={!authCtx.isLoggedIn}
                    />
                    <Button variant="outline-secondary" id="button-addon1" type="submit"
                            disabled={isLoading || !authCtx.isLoggedIn}>댓글 쓰기</Button>
                </InputGroup>
            </Form>
        </>
    )
}
