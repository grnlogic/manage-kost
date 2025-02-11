import { useState } from "react";
import { motion } from "framer-motion";
import { Button } from "../admin/button"; // Sesuaikan path jika perlu
import ConfettiExplosion from "react-confetti-explosion";

export default function BirthdaySurprise() {
  const [showSurprise, setShowSurprise] = useState(false);

  const handleClick = () => {
    setShowSurprise(true);
  };

  return (
    <div className="flex flex-col items-center justify-center h-screen bg-gradient-to-r from-blue-500 to-purple-500 text-white text-center p-4">
      <motion.h1
        className="text-4xl font-bold mb-6"
        initial={{ opacity: 0, y: -50 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 1 }}
      >
        🎉 Klik untuk Kejutan! 🎉
      </motion.h1>

      <Button
        onClick={handleClick}
        className="px-6 py-3 bg-yellow-400 text-black text-lg font-semibold rounded-lg shadow-lg hover:bg-yellow-300 transition"
      >
        Buka Kejutan 🎁
      </Button>

      {showSurprise && (
        <motion.div
          initial={{ scale: 0 }}
          animate={{ scale: 1 }}
          transition={{ duration: 0.5 }}
          className="mt-6 text-center"
        >
          <ConfettiExplosion />
          <motion.h2
            className="text-3xl font-bold mt-4"
            initial={{ opacity: 0, scale: 0.5 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ duration: 0.5 }}
          >
            Selamat Ulang Tahun! 🎂🎈
          </motion.h2>
          <motion.p
            className="text-lg mt-2"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 0.3 }}
          >
            Semoga semua impianmu tercapai dan harimu penuh kebahagiaan! maafkan
            aku yang terlambat mengucapkannya.
          </motion.p>
        </motion.div>
      )}
    </div>
  );
}
