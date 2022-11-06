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
import React, {useEffect, useState, Suspense} from "react";
import {boardsPageParams, boardsPageRequest} from "../../request/boardsRequest";
import {Simulate} from "react-dom/test-utils";
import load = Simulate.load;
import BoardListForm from "./board-list-element/BoardListElement";
import * as Events from "events";
import BoardCard from "./board-card/BoardCard";
import BoardBottom from "./board-bottom/BoardBottom";
import BoardSearch from "./board-search/BoardSearch";
import {boardsPageFetch} from "../../request/test/WrapPromise";
import {defaultParams} from "../../pages/board/Board";
import profile from "../../request/test/profile";
import {useQuery} from "react-query";
import {boardsPageRequest2} from "../../request/test/fetchBoards";

// let defaultParams = {
//     boardWriterNickname: null,
//     boardTitle: null,
//     boardContent: null,
//     boardCategory: "total",
//     page: 0,
//     sort: "regDate",
//     direction: "desc"
// }


export default function BoardForm(props: any) {
    // const [boardsResourse, setBoardsResource] = useState<any>(initialResource);
    // const response = boardsResourse.board.read();
    // const [params, setParams] = useState<boardsPageParams>(props.params);
    // const response = useQuery("boardsFetch", () => boardsPageRequest2(props.params), { suspense: true })
    // const response = props.resource.board.read();
    // const response = props.data
    const response = props.data
    const [boardsData, setBoardsData] = useState<any[]>([]);
    const [pageable, setPageable] = useState<any>([]);
    // console.log(boardsData)

    useEffect(() => {
        // setBoardsResource(boardsPageFetch(params))
        // let response = boardsResourse.board.read();
        const responseData = response.data;
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
    }, [response])

    let initialParams: boardsPageParams = {
        boardWriterNickname: props.params.boardWriterNickname,
        boardTitle: props.params.boardTitle,
        boardContent: props.params.boardContent,
        boardCategory: props.params.boardCategory,
        page: props.params.page,
        sort: props.params.sort,
        direction: props.params.direction,
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

    let initialSearchHandler = (boardTitle: string, boardContent: string, boardWriterNickname: string) => {
        initialParams.boardTitle = boardTitle;
        initialParams.boardContent = boardContent;
        initialParams.boardWriterNickname = boardWriterNickname;
    }

    const refreshHandler = () => {
        props.setRefresh(!props.refresh);
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
            <BoardCard initialParams={initialParams} initialCategoryHandler={initialCategoryHandler}
                       initialDirectionHandler={initialDirectionHandler} initialPageHandler={initialPageHandler}
                       initialSearchHandler={initialSearchHandler} setParams={props.setParams}
                       refreshHandler={refreshHandler}/>
            <BoardSearch initialParams={initialParams} initialSearchHandler={initialSearchHandler}
                         initialPageHandler={initialPageHandler} setParams={props.setParams} params={props.params}/>
            <div className="list-group list-group-flush">
                {boardList}
                {boardsData.length === 0 && <ListGroup.Item><h2>게시글이 없습니다</h2></ListGroup.Item>}
                <BoardBottom initialParams={initialParams} initialPageHandler={initialPageHandler}
                             setParams={props.setParams} pageable={pageable}/>
            </div>
        </>
    )
}
