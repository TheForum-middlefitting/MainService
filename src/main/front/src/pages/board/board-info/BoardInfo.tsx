import {useParams} from "react-router-dom";
import BoardInfoForm from "../../../components/board-info/BoardInfoForm";

export default function BoardInfo() {

    const params = useParams();

    return(
        <BoardInfoForm bookInfoId={{params}}/>
    )
}
