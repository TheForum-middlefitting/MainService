import axios from "axios";
// import qs from "qs";
// axios.defaults.paramsSerializer = params => {
//     return qs.stringify(params);
// }

export async function fetchUser() {
    return axios
        .get("https://jsonplaceholder.typicode.com/users/3")
        .then((res) => res.data)
        .catch((err) => console.log(err));
}
