package MenejementKos.DatabaseKos.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import MenejementKos.DatabaseKos.model.peraturan;
import MenejementKos.DatabaseKos.repository.peraturanRepository;

/**
 * Integration Test untuk Peraturan Module
 * 
 * Test ini melakukan:
 * 1. Test full flow dari HTTP Request → Controller → Service → Repository → Database
 * 2. Test CRUD operations (Create, Read, Update, Delete)
 * 3. Test validasi dan error handling
 * 
 * PENTING: Test ini menggunakan TRANSACTION ROLLBACK!
 * - Semua perubahan data di test akan di-ROLLBACK setelah test selesai
 * - Database asli TIDAK akan berubah
 * - Aman dijalankan di database development
 * 
 * @author KosApp Team
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@org.springframework.transaction.annotation.Transactional  // AUTO ROLLBACK!
public class PeraturanIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private peraturanRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private peraturan testPeraturan;

    @BeforeEach
    void setUp() {
        // Clean database before each test
        repository.deleteAll();
        
        // Buat data test
        testPeraturan = new peraturan();
        testPeraturan.setJudul_peraturan("Jam Malam");
        testPeraturan.setDeskripsi_peraturan("Tidak boleh keluar setelah jam 22:00");
    }

    @AfterEach
    void tearDown() {
        // Bersihkan database setelah test
        repository.deleteAll();
    }

    /**
     * TEST 1: Get All Peraturan (Empty Database)
     * Expected: Return empty array dengan status 200 OK
     */
    @Test
    @Order(1)
    @DisplayName("Test GET /api/peraturan - Empty Database")
    void testGetAllPeraturan_EmptyDatabase() throws Exception {
        mockMvc.perform(get("/api/peraturan"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /**
     * TEST 2: Create New Peraturan
     * Flow: POST request → Controller → Service → Repository → Database
     * Expected: Status 200 OK dan data tersimpan di database
     */
    @Test
    @Order(2)
    @DisplayName("Test POST /api/peraturan - Create New Peraturan")
    void testCreatePeraturan_Success() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(testPeraturan);

        MvcResult result = mockMvc.perform(post("/api/peraturan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.judul_peraturan").value("Jam Malam"))
                .andExpect(jsonPath("$.deskripsi_peraturan").value("Tidak boleh keluar setelah jam 22:00"))
                .andReturn();

        // Verifikasi data masuk ke database
        long count = repository.count();
        assertEquals(1, count, "Database should have 1 peraturan");

        // Verifikasi data di database sesuai
        peraturan savedPeraturan = repository.findAll().get(0);
        assertEquals("Jam Malam", savedPeraturan.getJudul_peraturan());
        assertEquals("Tidak boleh keluar setelah jam 22:00", savedPeraturan.getDeskripsi_peraturan());
    }

    /**
     * TEST 3: Get All Peraturan (With Data)
     * Expected: Return array dengan data yang ada
     */
    @Test
    @Order(3)
    @DisplayName("Test GET /api/peraturan - With Data")
    void testGetAllPeraturan_WithData() throws Exception {
        // Insert test data
        repository.save(testPeraturan);

        peraturan peraturan2 = new peraturan();
        peraturan2.setJudul_peraturan("Kebersihan");
        peraturan2.setDeskripsi_peraturan("Jaga kebersihan kamar");
        repository.save(peraturan2);

        // Test GET
        mockMvc.perform(get("/api/peraturan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].judul_peraturan", is("Jam Malam")))
                .andExpect(jsonPath("$[1].judul_peraturan", is("Kebersihan")));
    }

    /**
     * TEST 4: Get Peraturan By ID
     * Flow: GET with ID → Find in database → Return data
     */
    @Test
    @Order(4)
    @DisplayName("Test GET /api/peraturan/{id} - Success")
    void testGetPeraturanById_Success() throws Exception {
        // Save test data
        peraturan saved = repository.save(testPeraturan);

        // Test GET by ID
        mockMvc.perform(get("/api/peraturan/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.judul_peraturan").value("Jam Malam"));
    }

    /**
     * TEST 5: Get Peraturan By ID - Not Found
     * Expected: Return null atau 404
     */
    @Test
    @Order(5)
    @DisplayName("Test GET /api/peraturan/{id} - Not Found")
    void testGetPeraturanById_NotFound() throws Exception {
        mockMvc.perform(get("/api/peraturan/99999"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));  // Controller returns null
    }

    /**
     * TEST 6: Update Peraturan
     * Flow: PUT request → Update in database → Return updated data
     */
    @Test
    @Order(6)
    @DisplayName("Test PUT /api/peraturan/{id} - Update Success")
    void testUpdatePeraturan_Success() throws Exception {
        // Save original data
        peraturan saved = repository.save(testPeraturan);

        // Update data
        peraturan updatedData = new peraturan();
        updatedData.setJudul_peraturan("Jam Malam (Updated)");
        updatedData.setDeskripsi_peraturan("Tidak boleh keluar setelah jam 23:00");

        String jsonRequest = objectMapper.writeValueAsString(updatedData);

        // Test UPDATE
        mockMvc.perform(put("/api/peraturan/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.judul_peraturan").value("Jam Malam (Updated)"))
                .andExpect(jsonPath("$.deskripsi_peraturan").value("Tidak boleh keluar setelah jam 23:00"));

        // Verifikasi update di database
        peraturan updatedInDb = repository.findById(saved.getId()).orElse(null);
        assertNotNull(updatedInDb);
        assertEquals("Jam Malam (Updated)", updatedInDb.getJudul_peraturan());
    }

    /**
     * TEST 7: Delete Peraturan
     * Flow: DELETE request → Remove from database
     */
    @Test
    @Order(7)
    @DisplayName("Test DELETE /api/peraturan/{id} - Success")
    void testDeletePeraturan_Success() throws Exception {
        // Save test data
        peraturan saved = repository.save(testPeraturan);
        Long id = saved.getId();

        // Verify data exists
        assertTrue(repository.existsById(id));

        // Test DELETE
        mockMvc.perform(delete("/api/peraturan/" + id))
                .andExpect(status().isOk());

        // Verify data deleted
        assertFalse(repository.existsById(id));
        assertEquals(0, repository.count());
    }

    /**
     * TEST 8: Integration Test - Full CRUD Flow
     * Test complete workflow: Create → Read → Update → Delete
     */
    @Test
    @Order(8)
    @DisplayName("Test Complete CRUD Flow")
    void testCompleteCRUDFlow() throws Exception {
        // 1. CREATE
        String createJson = objectMapper.writeValueAsString(testPeraturan);
        MvcResult createResult = mockMvc.perform(post("/api/peraturan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isOk())
                .andReturn();

        peraturan created = objectMapper.readValue(
                createResult.getResponse().getContentAsString(), 
                peraturan.class
        );
        Long id = created.getId();

        // 2. READ
        mockMvc.perform(get("/api/peraturan/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.judul_peraturan").value("Jam Malam"));

        // 3. UPDATE
        created.setJudul_peraturan("Jam Malam Updated");
        String updateJson = objectMapper.writeValueAsString(created);
        mockMvc.perform(put("/api/peraturan/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.judul_peraturan").value("Jam Malam Updated"));

        // 4. DELETE
        mockMvc.perform(delete("/api/peraturan/" + id))
                .andExpect(status().isOk());

        // 5. VERIFY DELETED
        assertFalse(repository.existsById(id));
    }

    /**
     * TEST 9: Test Multiple Create Operations
     * Test creating multiple records in sequence
     */
    @Test
    @Order(9)
    @DisplayName("Test Multiple Create Operations")
    void testMultipleCreate() throws Exception {
        String[] juduls = {"Peraturan 1", "Peraturan 2", "Peraturan 3"};
        
        for (String judul : juduls) {
            peraturan p = new peraturan();
            p.setJudul_peraturan(judul);
            p.setDeskripsi_peraturan("Deskripsi " + judul);
            
            String json = objectMapper.writeValueAsString(p);
            mockMvc.perform(post("/api/peraturan")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk());
        }

        // Verify all created
        assertEquals(3, repository.count());
    }
}
