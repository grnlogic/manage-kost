import React, { useState } from "react";
import {
  BrowserRouter as Router,
  Route,
  Routes,
  useLocation,
} from "react-router-dom";
import Profile from "./component/Profile";
import Home from "./component/Home";
import Navbar from "./Navbar";
import LoginScreen from "./component/LoginScreen";
import FAQ from "./component/user/FAQ";
import Notification from "./component/notification";
import InfoKamar from "./component/user/InfoKamar";
import JadwalKebersihan from "./component/user/JadwalKebersihan";
import Pembayaran from "./component/user/pembayaran";
import Kompleks from "./component/admin/kompleks";

const Layout = ({
  setIsLoggedIn,
}: {
  setIsLoggedIn: React.Dispatch<React.SetStateAction<boolean>>;
}) => {
  const location = useLocation();

  return (
    <>
      {/* Render Navbar hanya jika bukan di halaman login */}
      {location.pathname !== "/" && <Navbar setIsLoggedIn={setIsLoggedIn} />}
      <div className="pt-0">
        <Routes>
          <Route
            path="/"
            element={<LoginScreen setIsLoggedIn={setIsLoggedIn} />}
          />
          <Route path="/home" element={<Home />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/faq" element={<FAQ />} />
          <Route path="/infoKamar" element={<InfoKamar />} />
          <Route path="/pembayaran" element={<Pembayaran />} />
          <Route path="/jadwal-kebersihan" element={<JadwalKebersihan />} />
          <Route path="/notification" element={<Notification />} />
          <Route path="/kompleks" element={<Kompleks />} />
        </Routes>
      </div>
    </>
  );
};

const App = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false); // Tambahkan state login

  return (
    <Router>
      <Layout setIsLoggedIn={setIsLoggedIn} />
    </Router>
  );
};

export default App;
