import NewBoardForm from "../../../components/new-board/NewBoardForm";
import {ErrorBoundary} from "react-error-boundary";
import {Suspense} from "react";
import {UserProfileFallback, UserProfileLoading} from "../../../components/TestError";
import LoadingSpinners from "../../../components/spinner/LoadingSpinner";

export default function NewBoard() {
    return (
        //<ErrorBoundary FallbackComponent={UserProfileFallback}>
        //    <Suspense fallback={<LoadingSpinners />}>
                <NewBoardForm/>
        //    </Suspense>
        //</ErrorBoundary>
    )
}
