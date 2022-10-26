import Badge from "react-bootstrap/Badge";
import React from "react";

// export interface boardInfo {
//     boardId : number;
// };

export default function BoarListElement(props : any) {
    return(
        <>
            <a href="#" className="list-group-item list-group-item-light flex-column align-items-start">
                <div className="d-flex w-100 justify-content-between">
                    <h6 className="mb-2">{props.nickname}</h6>
                    <small className="text-muted">3 days ago</small>
                </div>
                <div className="d-flex w-100">
                    <h4 className="mb-2">{props.title}</h4>
                </div>
                <div className="d-flex w-100 justify-content-between">
                    <h6><span><Badge pill bg="info">{props.category}</Badge></span>  #자바</h6>
                    <small className="text-muted">댓글:10 조회:10</small>
                </div>
            </a>
        </>
    )
}
