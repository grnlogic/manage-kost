# 🧪 Integration Testing Guide - KosApp Backend

## 📌 Apa itu Integration Testing?

Integration testing adalah **pengujian yang melibatkan beberapa komponen bekerja bersama**:

- ✅ Controller → Service → Repository → Database (full flow)
- ✅ HTTP Request → Response dengan data real
- ✅ Test CRUD operations end-to-end
- ✅ Verifikasi data tersimpan di database

## 🛠️ Setup Testing Environment

### 1️⃣ Persiapan Database Test

Buat database khusus untuk testing:

```sql
-- Buka pgAdmin atau psql
CREATE DATABASE kos_app_db_test;
```

### 2️⃣ File Configuration yang Sudah Dibuat

✅ `src/test/resources/application-test.yml` - Config untuk testing
✅ `src/test/java/.../integration/PeraturanIntegrationTest.java` - Test file

## 🚀 Cara Menjalankan Integration Test

### Method 1: Menggunakan Maven (Recommended)

```powershell
# Jalankan SEMUA test
cd d:\kos-app\manage-kost
mvn test

# Jalankan hanya Integration Test
mvn test -Dtest=PeraturanIntegrationTest

# Jalankan test dengan detail output
mvn test -Dtest=PeraturanIntegrationTest -X
```

### Method 2: Menggunakan VS Code

1. Buka file `PeraturanIntegrationTest.java`
2. Klik kanan pada class → **Run Tests**
3. Atau klik icon ▶️ di samping method test

### Method 3: Run Individual Test

```powershell
# Run satu test method saja
mvn test -Dtest=PeraturanIntegrationTest#testCreatePeraturan_Success
```

## 📊 Test Coverage

Test yang sudah dibuat untuk **Peraturan Module**:

| No  | Test Name                           | Deskripsi                       | Status |
| --- | ----------------------------------- | ------------------------------- | ------ |
| 1   | `testGetAllPeraturan_EmptyDatabase` | Test GET ketika database kosong | ✅     |
| 2   | `testCreatePeraturan_Success`       | Test POST create peraturan      | ✅     |
| 3   | `testGetAllPeraturan_WithData`      | Test GET dengan data            | ✅     |
| 4   | `testGetPeraturanById_Success`      | Test GET by ID                  | ✅     |
| 5   | `testGetPeraturanById_NotFound`     | Test GET ID tidak ada           | ✅     |
| 6   | `testUpdatePeraturan_Success`       | Test PUT update                 | ✅     |
| 7   | `testDeletePeraturan_Success`       | Test DELETE                     | ✅     |
| 8   | `testCompleteCRUDFlow`              | Test full CRUD flow             | ✅     |
| 9   | `testMultipleCreate`                | Test create multiple records    | ✅     |

## 🔍 Cara Membaca Hasil Test

### ✅ Test Sukses

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running MenejementKos.DatabaseKos.integration.PeraturanIntegrationTest
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
```

### ❌ Test Gagal

```
[ERROR] Tests run: 9, Failures: 2, Errors: 0, Skipped: 0
[ERROR] testCreatePeraturan_Success  Time elapsed: 0.5 s  <<< FAILURE!
Expected: status 200
Actual: status 500
```

## 🎯 Yang Ditest dalam Integration Test

### 1. **HTTP Request/Response**

```java
mockMvc.perform(get("/api/peraturan"))
    .andExpect(status().isOk())  // Status 200
    .andExpect(jsonPath("$", hasSize(0)));  // Response JSON
```

### 2. **Database Operations**

```java
// Create
peraturan saved = repository.save(testPeraturan);

// Verify
assertEquals(1, repository.count());
assertTrue(repository.existsById(saved.getId()));
```

### 3. **Complete Flow**

```
POST /api/peraturan
  ↓
Controller.addPeraturan()
  ↓
Service.savePeraturan()
  ↓
Repository.save()
  ↓
Database INSERT
  ↓
Return JSON Response
```

## 📝 Struktur Test Method

```java
@Test
@DisplayName("Test Description")
void testMethodName() throws Exception {
    // 1. ARRANGE - Setup data
    peraturan data = new peraturan();
    data.setJudul_peraturan("Test");

    // 2. ACT - Lakukan action
    mockMvc.perform(post("/api/peraturan")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))

    // 3. ASSERT - Verifikasi hasil
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists());

    // 4. VERIFY - Cek database
    assertEquals(1, repository.count());
}
```

## 🐛 Troubleshooting

### Problem 1: Database Connection Failed

```
Solution:
- Pastikan PostgreSQL running
- Cek database 'kos_app_db_test' sudah dibuat
- Verifikasi username/password di application-test.yml
```

### Problem 2: Test Failed - Data Not Found

```
Penyebab: Database masih ada data lama
Solution: Test otomatis clean database dengan @BeforeEach
```

### Problem 3: Port Already in Use

```
Solution: Config test pakai port random (server.port: 0)
```

## 📈 Next Steps - Buat Test untuk Module Lain

Anda bisa copy pattern ini untuk module lain:

### Test untuk Room Module

```java
@SpringBootTest
@AutoConfigureMockMvc
public class RoomIntegrationTest {
    // Test GET /api/rooms
    // Test POST /api/rooms
    // Test booking flow
}
```

### Test untuk User Authentication

```java
@SpringBootTest
@AutoConfigureMockMvc
public class AuthIntegrationTest {
    // Test login
    // Test register
    // Test token validation
}
```

### Test untuk Payment Flow

```java
@SpringBootTest
@AutoConfigureMockMvc
public class PaymentIntegrationTest {
    // Test create payment
    // Test verify payment
    // Test payment history
}
```

## 📚 Best Practices

1. ✅ **Isolasi Test** - Setiap test independen, clean database
2. ✅ **Test Real Scenario** - Test seperti user pakai aplikasi
3. ✅ **Verifikasi Database** - Jangan hanya cek response, cek data tersimpan
4. ✅ **Descriptive Names** - Nama test jelas: `testCreatePeraturan_Success`
5. ✅ **Test Edge Cases** - Test not found, validation error, dll

## 🎓 Resources

- [Spring Boot Testing](https://spring.io/guides/gs/testing-web/)
- [MockMvc Documentation](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/web/servlet/MockMvc.html)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)

## 📞 Support

Jika ada masalah saat running test:

1. Cek PostgreSQL sudah running
2. Cek database test sudah dibuat
3. Run `mvn clean test` untuk fresh start
4. Lihat log error di console

---

**Created**: November 11, 2025
**Last Updated**: November 11, 2025
