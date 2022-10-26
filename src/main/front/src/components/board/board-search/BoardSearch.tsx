import InputGroup from "react-bootstrap/InputGroup";
import DropdownButton from "react-bootstrap/DropdownButton";
import Dropdown from "react-bootstrap/Dropdown";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import React, {useState} from "react";

export default function BoardSearch(props : any) {

    const [tempInput, setTempInput] = useState("")
    const  [searchBy, setSearchBy] = useState<"title" | "content" | "nickname">("title")

    const searchByItem = {
        title : "제목",
        content : "내용",
        nickname : "닉네임"
    }

    const inputChangeHandler = (event: React.FormEvent<HTMLInputElement>) => {
        setTempInput(event.currentTarget.value);
    };

    let searchByHandler = (eventkey : any) => {
        if (eventkey === searchBy) {
            return;
        }
        setSearchBy(eventkey)
    }

    const searchHandler = () => {
        if(tempInput.trim().length === 0) {
            if (props.params.boardTitle || props.params.boardContent || props.params.boardWriterNickname) {
                props.initialSearchHandler(null, null, null)
                props.initialPageHandler(0);
                props.setParams(props.initialParams);
                return;
            }
            alert("검색어를 입력하세요!")
            return;
        }
        if (searchBy === "title") {
            props.initialSearchHandler(tempInput.trim(), null, null)
        }
        else if (searchBy === "content") {
            props.initialSearchHandler(null, tempInput.trim(), null)
        }
        else if (searchBy === "nickname") {
            props.initialSearchHandler(null, null, tempInput.trim())
        }
        props.initialPageHandler(0);
        props.setParams(props.initialParams);
    }

    const searchFormHandler = (event: React.FormEvent) => {
        event.preventDefault();
        searchHandler();
    }

    return (
                   <Form onSubmit={searchFormHandler} role={"search"}>
                    <InputGroup className="mb-3">
                        <DropdownButton
                            variant="outline-secondary"
                            title={searchByItem[searchBy]}
                            id="input-group-dropdown-1"
                            onSelect={searchByHandler}
                        >
                            <Dropdown.Item eventKey="title">제목</Dropdown.Item>
                            <Dropdown.Item eventKey="content">내용</Dropdown.Item>
                            <Dropdown.Item eventKey="nickname">닉네임</Dropdown.Item>
                        </DropdownButton>
                            <Form.Control aria-label="Text input with dropdown button" onInput={inputChangeHandler}/>
                        <Button variant="outline-secondary" id="button-addon1" onClick={searchHandler}>검색</Button>
                    </InputGroup>
                   </Form>
            //</div>
    )
}
