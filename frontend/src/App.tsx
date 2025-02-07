import React, { useState } from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Profile from "./component/Profile";
import Home from "./component/Home"; 
import Navbar from "./Navbar"; 
import LoginScreen from "./component/LoadingScreen"; 
import FAQ from "./component/user/FAQ";
import Notification from "./component/notification"; 

import InfoKamar from "./component/user/InfoKamar";
import JadwalKebersihan from "./component/user/JadwalKebersihan";
import Pembayaran from "./component/user/pembayaran"; 

const App = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(true);

  return (
    <Router>
      {/* Hanya render Navbar jika user sudah login */}
      {isLoggedIn && <Navbar setIsLoggedIn={setIsLoggedIn} />}

      <div className="pt-16">
        {/* Konten halaman */}
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/faq" element={<FAQ />} />
          <Route path="/infoKamar" element={<InfoKamar />} />
          <Route path="/pembayaran" element={<Pembayaran />} />
          <Route path="/jadwal-kebersihan" element={<JadwalKebersihan />} />
          <Route path="/notification" element={<Notification />} />
        </Routes>
      </div>
    </Router>
  );
};

export default App;
