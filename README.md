# Manajemen Kost

Aplikasi **Manajemen Kost** ini bertujuan untuk mempermudah pengelolaan kos-kosan, termasuk manajemen penghuni, pembayaran, dan tugas terkait pengelolaan kost. Aplikasi ini menggunakan **React (TSX)** untuk frontend dan **Spring Boot** dan **Strapi** untuk backend.

## Teknologi yang Digunakan

- **Frontend**:  
  - **React** (TSX)
  - **Tailwind CSS** untuk styling
  - **React Router** untuk routing
  - **Axios** untuk komunikasi HTTP

- **Backend**:  
  - **Spring Boot** (Java) untuk pengelolaan API
  - **Strapi** untuk CMS dan pengelolaan data yang lebih fleksibel

## Fitur

- **Manajemen Penghuni Kost**: Memungkinkan pemilik kost untuk menambah, mengedit, dan menghapus data penghuni.
- **Pembayaran**: Fitur untuk mencatat dan memverifikasi pembayaran sewa kost.
- **Manajemen Tugas**: Pemilik kost dapat membuat dan mengelola tugas atau pengingat untuk penghuni atau admin.
- **Autentikasi Pengguna**: Menggunakan JWT untuk login dan otorisasi pengguna.
- **Responsive**: Tampilan dapat diakses dengan baik di perangkat desktop maupun mobile.

## Instalasi

### 1. Frontend

Untuk memulai dengan frontend, Anda dapat meng-clone repo ini dan menjalankan aplikasi React:

```bash
# Clone repositori
git clone https://github.com/username/manajemen-kost.git

# Masuk ke direktori frontend
cd frontend

# Install dependencies
npm install

# Jalankan aplikasi
npm start


# Masuk ke direktori backend
cd backend

# Jalankan aplikasi
./mvnw spring-boot:run


# Masuk ke direktori Strapi
cd strapi

# Install dependencies
npm install

# Jalankan Strapi
npm run develop

