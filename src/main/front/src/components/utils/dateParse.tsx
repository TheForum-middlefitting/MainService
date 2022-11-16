export const getDate = (dateTime : string) => {
    const year : number = new Date(dateTime).getFullYear()
    const month : number = new Date(dateTime).getMonth() + 1
    const day: number = new Date(dateTime).getDay()
    return year + '-' + month + '-' + day
}

export const getDateTime = (dateTime : string) => {
    const year : number = new Date(dateTime).getFullYear()
    const month : number = new Date(dateTime).getMonth() + 1
    const day: number = new Date(dateTime).getDay()
    const hours = ('0' + new Date(dateTime).getHours()).slice(-2);
    const minutes =   ('0' + new Date(dateTime).getMinutes()).slice(-2);
    const seconds =   ('0' + new Date(dateTime).getSeconds()).slice(-2);
    return year + '-' + month + '-' + day + ' '  + hours + ':' + minutes  + ':' + seconds;
}

export const elapsedTime = (dateTime : string) => {
    const start = Date.parse(dateTime);
    const end = Date.parse(new Date().toString());
    const diff = (end - start);
    const times = [
        {time: "분", milliSeconds: 1000 * 60},
        {time: "시간", milliSeconds: 1000 * 60 * 60},
        {time: "일", milliSeconds: 1000 * 60 * 60 * 24},
        {time: "개월", milliSeconds: 1000 * 60 * 60 * 24 * 30},
        {time: "년", milliSeconds: 1000 * 60 * 60 * 24 * 365},
    ].reverse();
    for (const value of times) {
        const betweenTime = Math.floor(diff / value.milliSeconds);
        if (betweenTime > 0) {
            return `${betweenTime}${value.time} 전`;
        }
    }
    return "방금 전";
}
