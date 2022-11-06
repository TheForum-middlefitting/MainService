import {useParams} from "react-router-dom";
import BoardInfoForm from "../../../components/board-info/BoardInfoForm";
import {ErrorBoundary} from "react-error-boundary";
import {UserProfileFallback} from "../../../components/TestError";
import React, {Suspense} from "react";
import LoadingSpinners from "../../../components/spinner/LoadingSpinner";
import BoardForm from "../../../components/board/BoradForm";

export default function BoardInfo() {

    const params = useParams();

    return(
        <ErrorBoundary FallbackComponent={UserProfileFallback}>
            <Suspense fallback={<LoadingSpinners />}>
                <BoardInfoForm bookInfoId={{params}}/>
            </Suspense>
        </ErrorBoundary>
    )
}
