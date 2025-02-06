import logoKuning from "./image/logo kuning.svg"; // Import logo kuning
import { useState, useEffect } from "react";

const LoginScreen = () => {
  const [loading, setLoading] = useState(true);
  const [animate, setAnimate] = useState(false);
  const [logoMove, setLogoMove] = useState(false);
  const [contentMove, setContentMove] = useState(false);

  useEffect(() => {
    setTimeout(() => {
      setLoading(false); // Loading selesai
      setAnimate(true); // Jalankan animasi
      setLogoMove(true); // Logo mulai bergerak
      setTimeout(() => {
        setContentMove(true); // Konten welcome muncul setelah logo bergerak
      }, 500); // Delay sedikit untuk konten welcome muncul
    }, 3000); // 3 detik untuk loading
  }, []);

  return (
    <div className="h-screen w-full flex items-center justify-center bg-white relative overflow-hidden">
      {/* Lingkaran Kuning di Kanan Atas */}
      <div className="relative w-full h-screen">
        <div
          className={`absolute top-[-140px] right-[-310px] w-80 h-80 bg-[#FEBF00] rounded-full transition-opacity duration-1000 ${
            animate ? "opacity-0" : "opacity-100"
          }`}
        ></div>
      </div>

      {/* Lingkaran Kuning di Bawah Kiri */}
      <div className="relative w-full h-screen">
        <div
          className={`absolute bottom-[-120px] left-[-350px] w-80 h-80 bg-[#FEBF00] rounded-full transition-opacity duration-1000 ${
            animate ? "opacity-0" : "opacity-100"
          }`}
        ></div>
      </div>

      {/* Logo */}
      <div
        className={`flex justify-center items-center absolute top-0 left-0 right-0 bottom-0 transition-transform duration-1000 ${
          logoMove ? "translate-y-[-50px]" : ""
        }`}
      >
        <img src={logoKuning} alt="Logo" className="w-40 h-40" />
        {/* Logo berada di tengah */}
      </div>

      {/* Konten Welcome */}
      <div
        className={`absolute bottom-0 text-center bg-[#FEBF00] p-6 rounded-lg shadow-lg transition-transform duration-1000 ${
          contentMove ? "translate-y-0 opacity-100" : "translate-y-20 opacity-0"
        }`}
      >
        <h1 className="text-[49.312px] font-bold font-poppins leading-normal text-black text-left">
          Welcome
        </h1>

        <p className="mt-2 text-sm text-gray-800">
          Kelola kos lebih mudah dan praktis dalam satu aplikasi. Masuk ke akun
          Anda untuk memantau pembayaran, tugas, dan informasi kos dengan cepat
          dan aman.
        </p>
        <button className="mt-4 px-12 py-4 bg-black text-white rounded-lg w-full max-w-xs">
          Log In
        </button>
      </div>
    </div>
  );
};

export default LoginScreen;
