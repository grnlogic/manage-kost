import Kasur from "./asset/Kasur.svg";
import Brush from "./asset/Brush.svg";
import HelpCircle from "./asset/HelpCircle.svg";
import Kertas from "./asset/Kertas.svg";
import TOA from "./asset/TOA.svg";
import Money from "./asset/Money.svg";

const Beranda = () => {
  return (
    <div>
      <div className="w-full text-center bg-[#FEBF00] p-6 shadow-lg rounded-b-[30px]">
        <h1 className="flex items-center justify-center text-center text-2xl py-8 text-white font-bold">Beranda admin</h1>
      </div>
      <div className="absolute left-4 top-[200px] w-[150px] h-[130px] rounded-[8px] opacity-100 flex flex-col p-4 gap-y-2 bg-white border border-gray-200 box-border">
        <div className="flex items-center">
          <img src={Kasur} alt="Kasur" className="w-6 h-6 mr-2" />
          <p className="font-extrabold">Kamar</p>
        </div>
        <p className="text-start mt-2">Kelola informasi dan status kamar</p>
      </div>
      <div className="absolute right-4 top-[200px] w-[150px] h-[130px] rounded-[8px] opacity-100 flex flex-col p-4 gap-y-2 bg-white border border-gray-200 box-border">
        <div className="flex items-center">
          <img src={Brush} alt="Brush" className="w-6 h-6 mr-2" />
          <p className="font-extrabold">Kebersihan</p>
        </div>
        <p className="text-start mt-2">Kelola jadwal kebersihan</p>
      </div>
      <div className="absolute left-4 top-[350px] w-[150px] h-[130px] rounded-[8px] opacity-100 flex flex-col p-4 gap-y-2 bg-white border border-gray-200 box-border">
        <div className="flex items-center">
          <img src={HelpCircle} alt="Help Circle" className="w-6 h-6 mr-2" />
          <p className="font-extrabold">FAQ</p>
        </div>
        <p className="text-start mt-2">Kelola FAQ</p>
      </div>
      <div className="absolute right-4 top-[350px] w-[150px] h-[130px] rounded-[8px] opacity-100 flex flex-col p-4 gap-y-2 bg-white border border-gray-200 box-border">
        <div className="flex items-center">
          <img src={Kertas} alt="Kertas" className="w-6 h-6 mr-2" />
          <p className="font-extrabold">Peraturan</p>
        </div>
        <p className="text-start mt-2">Edit peraturan</p>
      </div>
      <div className="absolute left-4 top-[500px] w-[150px] h-[130px] rounded-[8px] opacity-100 flex flex-col p-4 gap-y-2 bg-white border border-gray-200 box-border">
        <div className="flex items-center">
          <img src={TOA} alt="TOA" className="w-6 h-6 mr-2" />
          <p className="font-extrabold text-sm">Pengumuman</p>
        </div>
        <p className="text-start mt-2">Edit pengumuman</p>
      </div>
      <div className="absolute right-4 top-[500px] w-[150px] h-[130px] rounded-[8px] opacity-100 flex flex-col p-4 gap-y-2 bg-white border border-gray-200 box-border">
        <div className="flex items-center">
          <img src={Money} alt="Money" className="w-6 h-6 mr-2" />
          <p className="font-extrabold text-sm">Pembayaran</p>
        </div>
        <p className="text-start mt-2">Kelola pembayaran</p>
      </div>
      <div className="absolute left-4 top-[650px] w-[150px] h-[130px] rounded-[8px] opacity-100 flex flex-col p-4 gap-y-2 bg-white border border-gray-200 box-border">
        <div className="flex items-center">
          <img src={TOA} alt="TOA" className="w-6 h-6 mr-2" />
          <p className="font-extrabold">Edit Akun</p>
        </div>
        <p className="text-start mt-2">Edit Akun Penghuni</p>
      </div>
      <div className="absolute right-4 top-[650px] w-[150px] h-[130px] rounded-[8px] opacity-100 flex flex-col p-4 gap-y-2 bg-white border border-gray-200 box-border">
        {/* Add your content here */}
      </div>
    </div>
  );
};

export default Beranda;