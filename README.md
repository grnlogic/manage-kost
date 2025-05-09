# Dalam tahap pengembangan

# Manajemen Kost

Aplikasi **Manajemen Kost** bertujuan untuk mempermudah pengelolaan kos-kosan, termasuk manajemen penghuni, pembayaran, dan tugas terkait pengelolaan kost. Aplikasi ini menggunakan **React (TSX)** untuk frontend dan **Spring Boot** untuk backend.

## 📌 Deskripsi

Backend aplikasi **Manajemen Kost** yang dibangun menggunakan **Spring Boot** untuk API utama.

## 🛠 Teknologi yang Digunakan

- **Spring Boot (Java)** – Backend utama
- **PostgreSQL/MySQL** – Database utama
- **Spring Security & JWT** – Autentikasi pengguna
- **Docker (Opsional)** – Untuk containerization

## 🚀 Instalasi dan Menjalankan Backend

### 1️⃣ Clone Repository

```bash
git clone https://github.com/grnlogic/manajemen-kost-backend.git
cd manajemen-kost-backend
```

### 2️⃣ Jalankan Backend Spring Boot

#### a. Pastikan Java & Maven sudah terinstal

```bash
java -version  # Pastikan Java 17+ terinstal
mvn -version   # Pastikan Maven terinstal
```

#### b. Install dependencies dan jalankan aplikasi

```bash
./mvnw spring-boot:run
```

Backend akan berjalan di `http://localhost:8080/`.

---

## 📁 Struktur Direktori

```plaintext
manajemen-kost-backend/
├── src/main/java/com/kost/  # Kode utama
├── src/main/resources/      # Configurations
├── pom.xml                  # Maven dependencies
├── database/                # SQL dump jika ada
└── README.md                # Dokumentasi backend
```

## 🔧 Pengembangan

Jika ingin berkontribusi:

1. **Fork** repository ini.
2. Buat **branch baru** (`feature/nama-fitur`).
3. Lakukan **commit** dan **push** perubahan ke branch tersebut.
4. Buat **Pull Request** untuk review.

## 📜 Lisensi

Proyek ini dilisensikan di bawah **MIT License**. Lihat file `LICENSE` untuk informasi lebih lanjut.
