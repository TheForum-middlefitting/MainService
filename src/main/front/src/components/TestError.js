import { Suspense } from 'react';
import { useQuery } from 'react-query';
import { ErrorBoundary } from 'react-error-boundary';
import axios from "axios";
import LoadingSpinners from "./spinner/LoadingSpinner";
const queryKey = 'user';
const queryFn = () => {
    return axios
        .get("https://jsonplaceholder.typicode.com/users/30000000")
        .then((res) => res.data)
        // .catch((err) => console.log(err));
}
export const UserProfile = () => {
    const { data } = useQuery(queryKey, queryFn, { suspense: true });
    return (
        <span>dddd </span>
    );
};
export const UserProfileFallback = ({ error, resetErrorBoundary }) => (
    <div>
        <p> 에러: {error.message} </p>
        <button onClick={() => resetErrorBoundary()}> 다시 시도 </button>
    </div>
);
export const UserProfileLoading = () => <div> 사용자 정보를 불러오는 중입니다. </div>;
const User = () => (
    <ErrorBoundary FallbackComponent={UserProfileFallback}>
        <Suspense fallback={<LoadingSpinners />}>
            <UserProfile />
            hello
        </Suspense>
    </ErrorBoundary>
);
export default User;
