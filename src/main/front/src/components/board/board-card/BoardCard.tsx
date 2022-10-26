import Card from "react-bootstrap/Card";
import Nav from "react-bootstrap/Nav";
import Button from "react-bootstrap/Button";
import AuthContext from "../../../store/auth-context";
import {useNavigate} from "react-router-dom";


import React, {useContext, useState} from "react";
import DropdownButton from "react-bootstrap/DropdownButton";
import Dropdown from "react-bootstrap/Dropdown";

export default function BoardCard(props: any) {
    const authCtx = useContext(AuthContext);
    const [direction, setDirection] = useState("desc");
    const navigate = useNavigate();

    let categoryHandler = (eventkey: any) => {
        if (eventkey === props.initialParams.boardCategory) {
            return
        }
        props.initialCategoryHandler(eventkey);
        props.initialPageHandler(0);
        props.initialSearchHandler(null, null, null);
        props.setParams(props.initialParams);
    }

    let directionHandler = (eventkey: any) => {
        if (eventkey === props.initialParams.direction) {
            return
        }
        props.initialDirectionHandler(eventkey);
        props.initialPageHandler(0);
        props.setParams(props.initialParams);
        setDirection(eventkey);
    }

    const toNewBoard = () => {
        navigate("/board/new");
    }

    return (
        <Card className={"mb-3"}>
            <Card.Header>
                <div className="d-flex w-200 justify-content-between">
                <Nav variant="tabs" defaultActiveKey="total" onSelect={categoryHandler}>
                    <Nav.Item>
                        <Nav.Link eventKey="total">전체게시판</Nav.Link>
                    </Nav.Item>
                    <Nav.Item>
                        <Nav.Link eventKey="free">자유게시판</Nav.Link>
                    </Nav.Item>
                    <Nav.Item>
                        <Nav.Link eventKey="notice">공지사항</Nav.Link>
                    </Nav.Item>
                </Nav>
                    <DropdownButton
                        variant="outline-secondary"
                        title={direction === "desc" ? "최신순" : "과거순"}
                        id="input-group-dropdown-1"
                        onSelect={directionHandler}
                    >
                        <Dropdown.Item eventKey="desc">최신순</Dropdown.Item>
                        <Dropdown.Item eventKey="asc">과거순</Dropdown.Item>
                    </DropdownButton>
                </div>
            </Card.Header>
            <Card.Body>
                <Card.Title>
                    <h2>전체게시판</h2>
                </Card.Title>
                <div className="d-flex w-200 justify-content-between">
                    {authCtx.isLoggedIn ? <Button variant="primary" onClick={toNewBoard}>글 작성하기</Button> :  <Button variant="secondary" disabled={false} onClick={toNewBoard}>로그인 후 작성</Button>}
                </div>
            </Card.Body>
        </Card>
    )
}
