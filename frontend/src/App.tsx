import React, { useState } from "react";
import {
  BrowserRouter as Router,
  Route,
  Routes,
  useLocation,
} from "react-router-dom";
import Profile from "./component/Profile";
import Home from "./component/Home";
import UserNavbar from "./Navbar";
import AdminNavbar from "./AdminNavbar";
import LoginScreen from "./component/LoginScreen";
import FAQ from "./component/user/FAQ";
import Notification from "./component/notification";
import InfoKamar from "./component/user/InfoKamar";
import JadwalKebersihan from "./component/user/JadwalKebersihan";
import Pembayaran from "./component/user/pembayaran";
import Kompleks from "./component/admin/kompleks";

//mengimport halaman admin
import Beranda from "./component/admin/Beranda";
import EditInfoKamar from "./component/admin/Edit Info Kamar";
import EditJadwalKebersihan from "./component/admin/Jadwal Kebersihan";
import EditPembayaran from "./component/admin/KelolaPembayaran";
import FAQAdmin from "./component/admin/FAQ Admin";
import EditPeraturan from "./component/admin/Edit Peraturan";
import EditPengumuman from "./component/admin/Pengumuman";
import EditAkunPenghuni from "./component/admin/AkunPenghuni";

const Layout = ({
  setIsLoggedIn,
  isAdmin,
  setIsAdmin,
}: {
  setIsLoggedIn: React.Dispatch<React.SetStateAction<boolean>>;
  isAdmin: boolean;
  setIsAdmin: React.Dispatch<React.SetStateAction<boolean>>;
}) => {
  const location = useLocation();

  return (
    <>
      {/* Render Navbar hanya jika bukan di halaman login dan bukan admin */}
      {location.pathname !== "/" && !isAdmin && (
        <UserNavbar setIsLoggedIn={setIsLoggedIn} />
      )}
      <div className="pt-0">
        <Routes>
          <Route
            path="/"
            element={<LoginScreen setIsLoggedIn={setIsLoggedIn} setIsAdmin={setIsAdmin} />}
          />
          <Route path="/home" element={<Home />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/faq" element={<FAQ />} />
          <Route path="/infoKamar" element={<InfoKamar />} />
          <Route path="/pembayaran" element={<Pembayaran />} />
          <Route path="/jadwal-kebersihan" element={<JadwalKebersihan />} />
          <Route path="/notification" element={<Notification />} />
          <Route path="/kompleks" element={<Kompleks />} />
          {/* Rute untuk halaman admin */}
          <Route path="/beranda" element={<Beranda />} />
          <Route path="/admin/edit-info-kamar" element={<EditInfoKamar />} />
          <Route path="/admin/edit-jadwal-kebersihan" element={<EditJadwalKebersihan />} />
          <Route path="/admin/edit-pembayaran" element={<EditPembayaran />} />
          <Route path="/admin/faq" element={<FAQAdmin />} />
          <Route path="/admin/edit-peraturan" element={<EditPeraturan />} />
          <Route path="/admin/edit-pengumuman" element={<EditPengumuman />} />
          <Route path="/admin/edit-akun-penghuni" element={<EditAkunPenghuni />} />
        </Routes>
      </div>
    </>
  );
};

const App = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false); // Tambahkan state login
  const [isAdmin, setIsAdmin] = useState(false); // Tambahkan state untuk admin

  return (
    <Router>
      <Layout setIsLoggedIn={setIsLoggedIn} isAdmin={isAdmin} setIsAdmin={setIsAdmin} />
    </Router>
  );
};

export default App;