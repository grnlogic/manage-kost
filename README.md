# Manajemen Kost

Aplikasi **Manajemen Kost** bertujuan untuk mempermudah pengelolaan kos-kosan, termasuk manajemen penghuni, pembayaran, dan tugas terkait pengelolaan kost. Aplikasi ini menggunakan **React (TSX)** untuk frontend dan **Spring Boot** serta **Strapi** untuk backend.

## Teknologi yang Digunakan

### Frontend
- **React (TSX)**
- **Tailwind CSS** untuk styling
- **React Router** untuk routing
- **Axios** untuk komunikasi HTTP

### Backend
- **Spring Boot (Java)** untuk pengelolaan API
- **Strapi** untuk CMS dan pengelolaan data yang lebih fleksibel

## Fitur

- **Manajemen Penghuni Kost**: Tambah, edit, dan hapus data penghuni.
- **Pembayaran**: Mencatat dan memverifikasi pembayaran sewa kost.
- **Manajemen Tugas**: Membuat dan mengelola tugas atau pengingat untuk penghuni atau admin.
- **Autentikasi Pengguna**: Menggunakan JWT untuk login dan otorisasi.
- **Responsif**: Tampilan dapat diakses dengan baik di perangkat desktop maupun mobile.

## Instalasi

### 1. Frontend
Untuk memulai dengan frontend, lakukan langkah-langkah berikut:

```bash
# Clone repositori
git clone https://github.com/username/manajemen-kost.git

# Masuk ke direktori frontend
cd frontend

# Install dependencies
npm install

# Jalankan aplikasi
npm start
```

### 2. Backend
#### a. Spring Boot
Pastikan Anda memiliki **Java 17 atau lebih tinggi** dan **Maven** terinstal di sistem Anda.

```bash
# Masuk ke direktori backend
cd backend

# Jalankan aplikasi
./mvnw spring-boot:run
```

#### b. Strapi
Pastikan Anda memiliki **Node.js dan npm** terinstal di sistem Anda.

```bash
# Masuk ke direktori Strapi
cd strapi

# Install dependencies
npm install

# Jalankan Strapi
npm run develop
```
Setelah aplikasi Strapi berjalan, Anda bisa mengakses admin panel di [http://localhost:1337/admin](http://localhost:1337/admin).

## Konfigurasi

### Konfigurasi Strapi
Setelah Strapi berjalan, Anda perlu membuat **Content Types** di admin panel sesuai kebutuhan aplikasi, seperti **Penghuni, Pembayaran, Tugas, dll**.

### Konfigurasi Spring Boot
Anda mungkin perlu mengatur konfigurasi database dan API di file `application.properties`.

## Struktur Direktori
```plaintext
manajemen-kost/
├── backend/          # Backend Spring Boot
├── frontend/         # Frontend React (TSX)
└── strapi/           # Backend CMS menggunakan Strapi
```

## Pengembangan
Jika Anda ingin berkontribusi dalam pengembangan aplikasi ini, silakan ikuti langkah-langkah berikut:

1. Fork repositori ini.
2. Buat branch baru untuk fitur/bugfix yang akan Anda kerjakan.
3. Kirim pull request setelah selesai.

## Lisensi
Proyek ini dilisensikan di bawah **MIT License**. Lihat file `LICENSE` untuk informasi lebih lanjut.

---

*Catatan: Silakan sesuaikan URL repo atau konfigurasi sesuai implementasi spesifik aplikasi Anda.*

