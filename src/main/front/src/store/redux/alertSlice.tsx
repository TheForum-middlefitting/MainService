import { createSlice } from '@reduxjs/toolkit';

const initialAlertState = { show: false, message: ""};

const alertSlice = createSlice({
    name: 'alert',
    initialState: initialAlertState,
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
    },
});

export const alertActions = alertSlice.actions;

export default alertSlice.reducer;
