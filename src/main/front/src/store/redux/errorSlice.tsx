import { createSlice } from '@reduxjs/toolkit';

const initialErrorState = { show: false, message: "", code: "", status: "" };

const errorSlice = createSlice({
    name: 'error',
    initialState: initialErrorState,
    reducers: {
        open(state) {
            state.show = true;
        },
        close(state) {
            state.show = false;
        },
        setMessage(state, action) {
            state.message = action.payload;
        },
        setCode(state, action) {
            state.code = action.payload;
        },
        setStatus(state, action) {
            state.status = action.payload;
        },
    },
});

export const errorActions = errorSlice.actions;

export default errorSlice.reducer;
