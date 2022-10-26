import {useContext, useEffect, useState} from "react";
import AuthContext from "../../../store/auth-context";
import {Link, useNavigate} from "react-router-dom";
import Form from "react-bootstrap/Form";
import DropdownButton from "react-bootstrap/DropdownButton";
import Dropdown from "react-bootstrap/Dropdown";
import {Container} from "react-bootstrap";
import Button from "react-bootstrap/Button";

export default function NewBoard() {
    const authCtx = useContext(AuthContext);
    const navigate = useNavigate();

    useEffect(() => {
        if (!authCtx.isLoggedIn) {
            navigate("/auth")
        }
    }, [authCtx, navigate]);

    return (
        <Form>
            <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                <Form.Label>제목</Form.Label>
                <Form.Control type="text" placeholder="제목을 입력해 주세요"/>
            </Form.Group>
            <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                <Form.Label>카테고리</Form.Label>
                <Form.Select aria-label="Default select example">
                    <option>카테고리를 선택해 주세요</option>
                    <option value="free">자유게시판</option>
                    <option value="notice">공지사항</option>
                </Form.Select>
            </Form.Group>
            <Form.Group controlId="formFileMultiple" className="mb-3">
                <Form.Label>사진</Form.Label>
                <Form.Control type="file" multiple/>
            </Form.Group>
            <Form.Group className="mb-3" controlId="exampleForm.ControlTextarea1">
                <Form.Label>본문</Form.Label>
                <Form.Control as="textarea" rows={10} placeholder="내용을 입력해 주세요"/>
            </Form.Group>
            <Form.Group className="d-flex justify-content-center mb-3" controlId="exampleForm.ControlTextarea1">
                <Button className={"me-3"} variant={"secondary"} type="submit">취소</Button>
                <Button className={"me-3"} type="submit">등록</Button>
            </Form.Group>
        </Form>
    )
}
