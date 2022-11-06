import {configureStore, getDefaultMiddleware} from '@reduxjs/toolkit';
import errorReducer from './errorSlice';
import warningReducer from "./warningSlice";
import alertReducer from "./alertSlice";


const store = configureStore({
    reducer: { warning: warningReducer, error: errorReducer, alert: alertReducer },
    // middleware: getDefaultMiddleware({
    //     serializableCheck: false,
    // }),
    middleware: (getDefaultMiddleware) => getDefaultMiddleware({
        serializableCheck: false,
    }),

});

export default store;
