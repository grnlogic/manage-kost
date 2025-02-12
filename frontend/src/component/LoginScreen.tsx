import logoKuning from "./image/logo kuning.svg"; // Logo kuning
import logoPutih from "./image/logo putih.svg"; // Logo putih
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom"; // Gunakan navigasi React Router

interface LoginScreenProps {
  setIsLoggedIn: React.Dispatch<React.SetStateAction<boolean>>;
}

const LoginScreen: React.FC<LoginScreenProps> = ({ setIsLoggedIn }) => {
  const [loading, setLoading] = useState(true);
  const [animate, setAnimate] = useState(true);
  const [logoMove, setLogoMove] = useState(false);
  const [logoColorChange, setLogoColorChange] = useState(false);
  const [showWelcome, setShowWelcome] = useState(false);
  const [contentMove, setContentMove] = useState(false);
  const [fadeOutWelcome, setFadeOutWelcome] = useState(false);
  const [showLoginForm, setShowLoginForm] = useState(false);
  const [showFormLoginAnimated, setShowFormLoginAnimated] = useState(false);

  const navigate = useNavigate(); // Gunakan untuk navigasi

  useEffect(() => {
    setTimeout(() => {
      setLoading(false);
      setLogoMove(true);

      setTimeout(() => {
        setShowWelcome(true);
        setTimeout(() => {
          setContentMove(true);
          setAnimate(false); // Menghilangkan bola kuning ketika konten welcome muncul
        }, 100);
      }, 500);
    }, 3000);
  }, []);

  const handleWelcomeLogin = () => {
    setFadeOutWelcome(true);

    setTimeout(() => {
      setShowLoginForm(true);
      setTimeout(() => {
        setShowFormLoginAnimated(true);
      }, 300);
    }, 1000);
  };

  const handleLogoChange = () => {
    setLogoMove(true);
    setTimeout(() => {
      setLogoColorChange(true);
    }, 500);
  };

  const handleLogin = () => {
    setIsLoggedIn(true); // Perbarui status login
    navigate("/home"); // Pindah ke halaman Home
  };

  return (
    <div
      className={`h-screen w-full flex items-center justify-center transition-all duration-1000 relative overflow-hidden ${
        showLoginForm ? "bg-[#FEBF00]" : "bg-white"
      }`}
    >
      {/* Lingkaran Kuning di Kanan Atas */}
      {!showLoginForm && (
        <div className="relative w-full h-screen">
          <div
            className={`absolute top-[-140px] right-[-310px] w-80 h-80 bg-[#FEBF00] rounded-full transition-opacity duration-1000 ${
              animate ? "opacity-100" : "opacity-0"
            }`}
          ></div>
        </div>
      )}

      {/* Lingkaran Kuning di Bawah Kiri */}
      {!showLoginForm && (
        <div className="relative w-full h-screen">
          <div
            className={`absolute bottom-[-200px] left-[-350px] w-80 h-80 bg-[#FEBF00] rounded-full transition-opacity duration-1000 ${
              animate ? "opacity-100" : "opacity-0"
            }`}
          ></div>
        </div>
      )}

      {/* Logo */}
      <div
        className={`flex justify-center items-center absolute top-0 left-0 right-0 bottom-0 transition-transform duration-1000 ${
          logoMove ? "translate-y-[-20px]" : "translate-y-0"
        }`}
      >
        <img
          src={logoKuning}
          alt="Logo Kuning"
          className={`w-40 h-40 transition-all duration-1000 ${
            logoColorChange ? "opacity-0" : "opacity-100"
          }`}
        />

        <img
          src={logoPutih}
          alt="Logo Putih"
          className={`w-40 h-40 absolute transition-all duration-1000 ${
            logoColorChange ? "opacity-100 translate-y-[-200px]" : "opacity-0"
          }`}
        />
      </div>

      {/* Konten Welcome */}
      {showWelcome && !showLoginForm && (
        <div
          className={`absolute bottom-0 text-center bg-[#FEBF00] p-6 rounded-lg shadow-lg transition-all duration-1000 ${
            fadeOutWelcome
              ? "opacity-0 translate-y-20"
              : contentMove
              ? "opacity-100 translate-y-0"
              : "opacity-0 translate-y-20"
          }`}
        >
          <h1 className="text-[49.312px] font-bold font-poppins leading-normal text-black text-left">
            Welcome
          </h1>

          <p className="mt-2 text-sm text-gray-800">
            Kelola kos lebih mudah dan praktis dalam satu aplikasi. Masuk ke
            akun Anda untuk memantau pembayaran, tugas, dan informasi kos dengan
            cepat dan aman.
          </p>
          <button
            onClick={() => {
              handleWelcomeLogin();
              handleLogoChange();
            }}
            className="mt-4 px-12 py-4 bg-black text-white rounded-lg w-full max-w-xs"
          >
            Log In
          </button>
        </div>
      )}

      {/* Form Login */}
      {showLoginForm && (
        <div
          className={`absolute bottom-0 text-center bg-white p-6 rounded-lg shadow-lg transition-all duration-1000 ${
            showFormLoginAnimated
              ? "opacity-100 translate-y-0"
              : "opacity-0 translate-y-20"
          }`}
        >
          <div className="space-y-4 mt-8">
            <div className="bg-white p-6 rounded-xl shadow pb-10">
              <input
                type="text"
                placeholder="Username"
                className="w-[300px] p-5 text-lg border border-orange-200 rounded-lg mb-7"
              />

              <input
                type="password"
                placeholder="Password"
                className="w-[300px] p-5 text-lg border border-orange-200 rounded-lg mb-7"
              />

              <button
                className="mt-4 px-12 py-4 bg-black text-white rounded-lg w-full max-w-xs"
                onClick={handleLogin} // Gunakan fungsi login yang benar
              >
                Log In
              </button>
              <p className="text-center mt-4 text-orange-600">Lupa Password?</p>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default LoginScreen;
