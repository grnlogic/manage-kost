import React, { useState } from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Profile from "./component/Profile";
import Home from "./component/Home"; // <- Ini adalah komponen halaman Home
import Navbar from "./Navbar"; // Menambahkan import Navbar
import LoginScreen from "./component/LoadingScreen"; // Menambahkan import LoginScreen

const App = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(true);

  return (
    <Router>
      {/* Hanya render Navbar jika user sudah login */}
      {isLoggedIn && <Navbar setIsLoggedIn={setIsLoggedIn} />}

      <div className="pt-16 ">
        {/* Konten halaman */}
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/profile" element={<LoginScreen />} />
        </Routes>
      </div>
    </Router>
  );
};

export default App;
