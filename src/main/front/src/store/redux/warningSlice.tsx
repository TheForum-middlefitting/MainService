import { createSlice } from '@reduxjs/toolkit';

const initialWarningState = { show: false, message: "", executeFunction: null };

const warningSlice = createSlice({
    name: 'warning',
    initialState: initialWarningState,
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
        setFunction(state, action) {
            state.executeFunction = action.payload;
        },

    },
});

export const warningActions = warningSlice.actions;

export default warningSlice.reducer;
