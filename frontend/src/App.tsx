import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Profile from "./component/Profile";
import Home from "./component/Home";
import Navbar from "./Navbar";
import LoginScreen from "./component/LoginScreen";
import FAQ from "./component/user/FAQ";
import Notification from "./component/notification";
import InfoKamar from "./component/user/InfoKamar";
import JadwalKebersihan from "./component/user/JadwalKebersihan";
import Pembayaran from "./component/user/pembayaran";

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LoginScreen />} />
        <Route path="/home" element={<Home />} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/faq" element={<FAQ />} />
        <Route path="/infoKamar" element={<InfoKamar />} />
        <Route path="/pembayaran" element={<Pembayaran />} />
        <Route path="/jadwal-kebersihan" element={<JadwalKebersihan />} />
        <Route path="/notification" element={<Notification />} />
      </Routes>
    </Router>
  );
};

export default App;