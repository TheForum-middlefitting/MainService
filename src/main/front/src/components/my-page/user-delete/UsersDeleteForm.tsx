import {Container} from "react-bootstrap";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import React, {useContext, useState} from "react";
import AuthContext from "../../../store/context/auth-context";
import {useDispatch} from "react-redux";
import {errorActions} from "../../../store/redux/errorSlice";
import {alertActions} from "../../../store/redux/alertSlice";
import {warningActions} from "../../../store/redux/warningSlice";
import {useMutation, useQueryClient} from "react-query";
import {deleteUsersRequest, updateUsersRequest, usersUpdateParams} from "../../../request/usersRequest";
import {useNavigate} from "react-router-dom";

export default function UsersDeleteForm() {

    const [enteredPassword, setEnteredPassword] = useState("")
    const queryClient = useQueryClient();
    const authCtx = useContext(AuthContext);
    const dispatch = useDispatch();
    const navigate = useNavigate()

    const deleteUsersMutation = useMutation((password: string) => deleteUsersRequest(authCtx.id.toString(), password, authCtx.authorization), {
        onSuccess: (response) => {
            queryClient.invalidateQueries(["boardsCommentsPage"],
                { refetchInactive: true })
            queryClient.invalidateQueries(["boardsPage"],
                { refetchInactive: true })
                .then(r => {
                    authCtx.logout()
                    navigate("/home")
                    dispatch(alertActions.setMessage("회원탈퇴를 완료하였습니다!"));
                    dispatch(alertActions.open());
                })
        },
        onError: (error: any) => {
            dispatch(alertActions.setMessage(error.response.data.message));
            dispatch(alertActions.open());
        },
        onSettled: () => {
            setEnteredPassword("")
        }
    });

    const deleteUserActions = () => {
        deleteUsersMutation.mutate(enteredPassword)
    }

    const deleteUserHandler = (event : React.FormEvent) => {
        event.preventDefault();
        dispatch(warningActions.setMessage("회원탈퇴를 진행합니다!"));
        dispatch(warningActions.open());
        dispatch(warningActions.setFunction(deleteUserActions))
    }

    const passwordChangeHandler = (event: any) => {
        setEnteredPassword(event.currentTarget.value);
    };

    const enteredPasswordParseCheck = () => {
        return !(enteredPassword.trim().length < 10 || enteredPassword.trim().length > 20);
    }

    return (
        <>
            <Container
                className={"mx-auto my-3 container-sm"}
                style={{maxWidth: "540px"}}
            >
                <br/>
                <h2 className={"text-center"}>
                    <strong>회원탈퇴</strong>
                </h2>
                <h5>
                    <div className={"m-3 text-center"}>
                        <p className="text-break">
                            회원탈퇴를 진행합니다.
                        </p>
                    </div>
                </h5>
                <hr style={{
                    height: "2px",
                    color: "black",
                    background: "black"
                }}/>
                <Form onSubmit={deleteUserHandler}>
                    <Form.Group className="mb-3">
                        <Form.Label><strong>패스워드</strong></Form.Label>
                        <Form.Control
                            placeholder="비밀번호를 입력해 주세요"
                            id="password"
                            type="password"
                            required={true}
                            value={enteredPassword}
                            onChange={passwordChangeHandler}
                        />
                    </Form.Group>
                    <div className="d-grid mb-3">
                        <Button
                            type="submit"
                            disabled={!enteredPasswordParseCheck()}>
                            회원탈퇴
                        </Button>
                    </div>
                </Form>
            </Container>
        </>
    )
}
