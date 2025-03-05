# Menggunakan image node.js sebagai base image
FROM node:16

# Menentukan direktori kerja di dalam container
WORKDIR /app

# Menyalin file package.json dan package-lock.json ke dalam container
COPY package*.json ./

# Menginstal dependensi aplikasi dengan mengabaikan konflik peer-dependencies
RUN npm install --legacy-peer-deps

# Menyalin semua file proyek ke dalam container
COPY . .

# Menentukan perintah yang dijalankan saat container berjalan
CMD ["npm", "start"]

# Menentukan port yang digunakan oleh aplikasi
EXPOSE 3000

