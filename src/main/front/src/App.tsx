import React, {useEffect, useState} from 'react';
import './App.css';
import {Route, Routes} from "react-router-dom";
import Home from "./pages/Home";
import HeaderForm from "./components/layout/header/HeaderForm";
import AuthPage from "./pages/AuthPage";
import {Navigate} from "react-router-dom";
import axios from "axios";

function App() {

  const [hello, setHello] = useState('')

  useEffect(() => {
    axios.get('/boards/1')
        .then(response => setHello(response?.data?.data?.content))
        .catch(error => console.log(error))
  }, []);


  return (
      <>
        <header><HeaderForm/></header>
    <main>
      <Routes>
        <Route path="/" element={<Navigate to="/home" />} />
        <Route path="/home" element={<Home />} />
        <Route path="/auth" element={<AuthPage />} />
      </Routes>
    </main>
        {hello}
      </>
  );
}

export default App;
