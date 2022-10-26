import classes from "./BoardForm.module.css"
import {useLocation} from "react-router-dom";
import Button from "react-bootstrap/Button";
import Badge from "react-bootstrap/Badge";
import Card from "react-bootstrap/Card";
import Nav from "react-bootstrap/Nav";
import Dropdown from "react-bootstrap/Dropdown";
import DropdownButton from "react-bootstrap/DropdownButton";
import InputGroup from "react-bootstrap/InputGroup";
import Form from "react-bootstrap/Form";


import NavDropdown from "react-bootstrap/NavDropdown";
import ListGroup from "react-bootstrap/ListGroup";
import React, {useEffect, useState} from "react";
import {boardsPageParams, boardsPageRequest} from "./request/boardRequest";
import {Simulate} from "react-dom/test-utils";
import load = Simulate.load;
import BoardListForm from "./board-list-element/BoardListElement";
import * as Events from "events";
import BoardCard from "./board-card/BoardCard";
import BoardBottom from "./board-bottom/BoardBottom";
import BoardSearch from "./board-search/BoardSearch";

export default function BoardForm(props: any) {
    const [boardsData, setBoardsData] = useState<any[]>([]);
    const [params, setParams] = useState<boardsPageParams>({
        boardWriterNickname: null,
        boardTitle: null,
        boardContent: null,
        boardCategory: props.category,
        page: 0,
        sort: "regDate",
        direction : "desc"
    });
    const [pageable, setPageable] = useState<any>([]);

    useEffect(() => {
        const getBoards = async () => {
            const response = await boardsPageRequest(params);
            const responseData = await response.data;
            // console.log(responseData);
            const loadedBoards = [];
            if (response.status === 200) {
                const content = responseData.content;
                for (const key in responseData.content) {
                    loadedBoards.push({
                        id: content[key].boardId,
                        category: content[key].boardCategory,
                        memberId: content[key].memberId,
                        nickname: content[key].nickname,
                        title: content[key].title,
                    })
                }
                setBoardsData(loadedBoards);
                let pageable = responseData.pageable
                pageable.last = responseData.last;
                pageable.first = responseData.first;
                pageable.totalPages = responseData.totalPages;
                pageable.totalElements = responseData.totalElements;
                pageable.number = responseData.number;
                setPageable(pageable)
            }
        }
        getBoards();
    }, [params])

    let initialParams: boardsPageParams = {
        boardWriterNickname: params.boardWriterNickname,
        boardTitle: params.boardTitle,
        boardContent: params.boardContent,
        boardCategory: params.boardCategory,
        page: params.page,
        sort: params.sort,
        direction : params.direction,
    }

    let initialCategoryHandler = (category: string) => {
        initialParams.boardCategory = category;
    }

    let initialDirectionHandler = (direction: string) => {
        initialParams.direction = direction;
    }

    let initialPageHandler = (page: number) => {
        initialParams.page = page;
    }

    let initialSearchHandler = (boardTitle : string, boardContent : string, boardWriterNickname : string) => {
        initialParams.boardTitle = boardTitle;
        initialParams.boardContent = boardContent;
        initialParams.boardWriterNickname = boardWriterNickname;
    }

    const boardList = boardsData.map((board) => {
        return <BoardListForm
            key={board.id}
            id={board.id}
            category={board.category}
            memberId={board.memberId}
            nickname={board.nickname}
            title={board.title}
        />
    })

    return (
        <>
            <BoardCard initialParams={initialParams} initialCategoryHandler={initialCategoryHandler} initialDirectionHandler={initialDirectionHandler} initialPageHandler={initialPageHandler} initialSearchHandler={initialSearchHandler} setParams={setParams}/>
            <BoardSearch initialParams={initialParams} initialSearchHandler={initialSearchHandler} initialPageHandler={initialPageHandler} setParams={setParams} params={params}/>
            <div className="list-group">
                {boardList}
                {boardsData.length === 0 && <ListGroup.Item><h2>게시글이 없습니다</h2></ListGroup.Item>}
                <BoardBottom initialParams={initialParams} initialPageHandler={initialPageHandler} setParams={setParams} pageable={pageable}/>
            </div>
        </>
    )
}
