import { Calendar, CreditCard, HelpCircle, Key } from "lucide-react";

const Home = () => {
  return (
    <div className="w-full flex flex-col items-center">
      {/* Header Oranye */}
      <div className="w-full text-center bg-[#FEBF00] p-6 shadow-lg rounded-b-[30px]">
        {/* Lingkaran abu-abu di tengah */}
        <div className="mx-auto mt-4 w-24 h-24 bg-gray-400 rounded-full"></div>
        <h1 className="mt-4 text-white font-bold text-2xl">SELAMAT DATANG</h1>
        <h2 className="mt-1 text-white font-bold text-base">Fajar Geran Arifin</h2>
      </div>

      {/* Tombol Menu */}
      <div className="text-center px-6 mt-6 grid grid-cols-2 gap-4 place-items-center">
        <MenuButton icon={<Key size={40} />} text="Info Kamar" />
        <MenuButton icon={<Calendar size={40} />} text="Pembayaran" />
        <MenuButton icon={<CreditCard size={40} />} text="Jadwal Kebersihan" />
        <MenuButton icon={<HelpCircle size={40} />} text="FAQ" />
      </div>
    </div>
  );
};

// Komponen Reusable untuk Tombol
const MenuButton = ({ icon, text }: { icon: JSX.Element; text: string }) => {
  return (
    <button className="px-6 py-3 bg-[#FEBF00] text-white rounded-lg w-full max-w-[180px] font-bold flex items-center justify-center gap-4 shadow-md transition-transform hover:scale-105">
      {/* Ukuran ikon yang konsisten */}
      <div className="w-8 h-8 flex items-center justify-center">
        {icon}
      </div>
      {/* Teks dengan ukuran konsisten */}
      <span className="text-sm">{text}</span>
    </button>
  );
};

export default Home;
