import Badge from "react-bootstrap/Badge";
import React from "react";
import {useNavigate} from "react-router-dom";

export default function BoarListElement(props : any) {
    const navigate = useNavigate();

    const toSingleBoardsPage = () => {
        navigate(`/boards/info/${props.id}`,  {state : {
                // id : props.id
            }
        });
    }

    const categoryParsing = (category : string) => {
        switch (category) {
            case 'total' :
                return '전체';
            case 'free' :
                return '자유';
            case 'notice' :
                return '공지';
        }
    }

    return(
        <>
            <a style={{cursor: "pointer"}} onClick={toSingleBoardsPage} className="list-group-item list-group-item-light flex-column align-items-start">
                <div className="d-flex w-100 justify-content-between">
                    <p className="mb-2"><small>{props.nickname}</small></p>
                    <p><small className="text-muted">3 days ago</small></p>
                </div>
                <div className="d-flex w-100">
                    <h6 className="mb-2"><strong>{props.title}</strong></h6>
                </div>
                <div className="d-flex w-100 justify-content-between">
                    <p><span><Badge pill bg="info" className="me-2">{categoryParsing(props.category)}</Badge></span><small>#자바</small></p>
                    <p><small className="text-muted">댓글:10 조회:10</small></p>
                </div>
            </a>
        </>
    )
}
