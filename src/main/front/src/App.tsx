import React, {useEffect, useState} from 'react';
import './App.css';
import {Route, Routes} from "react-router-dom";
import Home from "./pages/Home";
import HeaderForm from "./components/layout/header/HeaderForm";
import AuthPage from "./pages/AuthPage";
import {Navigate} from "react-router-dom";
import axios from "axios";
import SignUpPage from "./pages/SIgnUpPage";
import Board from "./pages/board/Board";
import "bootstrap/dist/css/bootstrap.min.css";
import {Container, ThemeProvider} from "react-bootstrap";
import Study from "./components/Study";
import BoardInfo from "./pages/board/board-info/BoardInfo";
import NewBoard from "./pages/board/new-board/NewBoard";


function App() {
  return (
      <>
        <ThemeProvider
            breakpoints={['xxxl', 'xxl', 'xl', 'lg', 'md', 'sm', 'xs', 'xxs']}
            minBreakpoint="xxs"
        >
            {/*<Container>*/}
                <header><HeaderForm/></header>
            {/*</Container>*/}
    <main>
        <Container className={"mx-auto my-3"}>
            <Routes>
                <Route path="/" element={<Navigate to="/home" />} />
                <Route path="/home" element={<Home />} />
                <Route path="/auth" element={<AuthPage />} />
                <Route path="/sign-up" element={<SignUpPage />} />
                <Route path="/board/:category" element={<Board />}/>
                <Route path="/board/board-info/:boardsId" element={<BoardInfo />}/>
                <Route path="/board/new" element={<NewBoard />}/>
                <Route path="/study" element={<Study/>}/>
            </Routes>
        </Container>

    </main>
        </ThemeProvider>
      </>
  );
}

export default App;
