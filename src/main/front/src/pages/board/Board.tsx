import classes from "../Board.module.css"
import BoardForm from "../../components/board/BoradForm";
import {useParams} from "react-router-dom";

type BoardProps = {
    category : string
}

export default function Board() {
    const params = useParams();
    return(
        // <div className={classes.input}>
            <BoardForm category={params.category}/>
        //</div>
    );
}
