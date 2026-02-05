package MenejementKos.DatabaseKos.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import MenejementKos.DatabaseKos.model.kamar;
import MenejementKos.DatabaseKos.repository.KamarRepository;

/**
 * White-box Testing untuk KamarService
 * Testing berbagai path, kondisi validasi, dan edge cases
 */
@ExtendWith(MockitoExtension.class)
class KamarServiceTest {

    @Mock
    private KamarRepository kamarRepository;

    @InjectMocks
    private KamarService kamarService;

    private kamar validKamar;

    @BeforeEach
    void setUp() {
        validKamar = new kamar();
        validKamar.setId(1L);
        validKamar.setNomorKamar("101");
        validKamar.setHargaBulanan(1500000.0);
        validKamar.setStatus("kosong");
        validKamar.setFasilitas("AC, WiFi");
        validKamar.setTitle("Kamar 101");
        validKamar.setDescription("Kamar kost nomor 101");
        validKamar.setPrice(1500000.0);
    }

    /**
     * Test Case 1: Get all kamar - path normal
     */
    @Test
    void testGetAllKamar_Success() {
        List<kamar> kamarList = Arrays.asList(validKamar, new kamar());
        when(kamarRepository.findAll()).thenReturn(kamarList);

        List<kamar> result = kamarService.getAllKamar();

        assertEquals(2, result.size());
        verify(kamarRepository, times(1)).findAll();
    }

    /**
     * Test Case 2: Get kamar by ID - kamar ditemukan
     * Path: getKamarById -> Optional.of(kamar)
     */
    @Test
    void testGetKamarById_Found() {
        when(kamarRepository.findById(1L)).thenReturn(Optional.of(validKamar));

        Optional<kamar> result = kamarService.getKamarById(1L);

        assertTrue(result.isPresent());
        assertEquals("101", result.get().getNomorKamar());
        verify(kamarRepository, times(1)).findById(1L);
    }

    /**
     * Test Case 3: Get kamar by ID - kamar tidak ditemukan
     * Path: getKamarById -> Optional.empty()
     */
    @Test
    void testGetKamarById_NotFound() {
        when(kamarRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<kamar> result = kamarService.getKamarById(999L);

        assertFalse(result.isPresent());
        verify(kamarRepository, times(1)).findById(999L);
    }

    /**
     * Test Case 4: Save kamar valid dengan semua field lengkap
     * Path: saveKamar -> validasi pass -> set defaults (minimal) -> save
     */
    @Test
    void testSaveKamar_ValidWithAllFields() {
        // Kamar baru tanpa ID
        validKamar.setId(null);
        
        when(kamarRepository.existsByNomorKamar("101")).thenReturn(false);
        when(kamarRepository.save(any(kamar.class))).thenReturn(validKamar);

        kamar result = kamarService.saveKamar(validKamar);

        assertNotNull(result);
        assertEquals("101", result.getNomorKamar());
        verify(kamarRepository, times(1)).save(any(kamar.class));
        verify(kamarRepository, times(1)).existsByNomorKamar("101");
    }

    /**
     * Test Case 5: Save kamar dengan nomor kamar null
     * Path: saveKamar -> validasi fail (nomor kamar null) -> exception
     */
    @Test
    void testSaveKamar_NullNomorKamar() {
        kamar invalidKamar = new kamar();
        invalidKamar.setNomorKamar(null);
        invalidKamar.setHargaBulanan(1500000.0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            kamarService.saveKamar(invalidKamar);
        });

        assertEquals("Nomor kamar tidak boleh kosong", exception.getMessage());
        verify(kamarRepository, never()).save(any());
    }

    /**
     * Test Case 6: Save kamar dengan nomor kamar empty string
     * Path: saveKamar -> validasi fail (nomor kamar empty) -> exception
     */
    @Test
    void testSaveKamar_EmptyNomorKamar() {
        kamar invalidKamar = new kamar();
        invalidKamar.setNomorKamar("   ");
        invalidKamar.setHargaBulanan(1500000.0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            kamarService.saveKamar(invalidKamar);
        });

        assertEquals("Nomor kamar tidak boleh kosong", exception.getMessage());
        verify(kamarRepository, never()).save(any());
    }

    /**
     * Test Case 7: Save kamar dengan harga <= 0
     * Path: saveKamar -> validasi fail (harga invalid) -> exception
     */
    @Test
    void testSaveKamar_InvalidPrice_Zero() {
        kamar invalidKamar = new kamar();
        invalidKamar.setNomorKamar("102");
        invalidKamar.setHargaBulanan(0.0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            kamarService.saveKamar(invalidKamar);
        });

        assertEquals("Harga bulanan harus lebih dari 0", exception.getMessage());
        verify(kamarRepository, never()).save(any());
    }

    /**
     * Test Case 8: Save kamar dengan harga negatif
     * Path: saveKamar -> validasi fail (harga negatif) -> exception
     */
    @Test
    void testSaveKamar_InvalidPrice_Negative() {
        kamar invalidKamar = new kamar();
        invalidKamar.setNomorKamar("102");
        invalidKamar.setHargaBulanan(-100000.0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            kamarService.saveKamar(invalidKamar);
        });

        assertEquals("Harga bulanan harus lebih dari 0", exception.getMessage());
    }

    /**
     * Test Case 9: Save kamar dengan status null - auto set default
     * Path: saveKamar -> status null -> set "kosong"
     */
    @Test
    void testSaveKamar_NullStatus_SetDefault() {
        kamar kamarWithoutStatus = new kamar();
        kamarWithoutStatus.setNomorKamar("103");
        kamarWithoutStatus.setHargaBulanan(1500000.0);
        kamarWithoutStatus.setStatus(null);

        when(kamarRepository.save(any(kamar.class))).thenAnswer(i -> i.getArgument(0));
        when(kamarRepository.existsByNomorKamar("103")).thenReturn(false);

        kamar result = kamarService.saveKamar(kamarWithoutStatus);

        assertEquals("kosong", result.getStatus());
        verify(kamarRepository, times(1)).save(any(kamar.class));
    }

    /**
     * Test Case 10: Save kamar dengan fasilitas null - auto set default
     * Path: saveKamar -> fasilitas null -> set empty string
     */
    @Test
    void testSaveKamar_NullFasilitas_SetDefault() {
        kamar kamarWithoutFasilitas = new kamar();
        kamarWithoutFasilitas.setNomorKamar("104");
        kamarWithoutFasilitas.setHargaBulanan(1500000.0);
        kamarWithoutFasilitas.setFasilitas(null);

        when(kamarRepository.save(any(kamar.class))).thenAnswer(i -> i.getArgument(0));
        when(kamarRepository.existsByNomorKamar("104")).thenReturn(false);

        kamar result = kamarService.saveKamar(kamarWithoutFasilitas);

        assertEquals("", result.getFasilitas());
        verify(kamarRepository, times(1)).save(any(kamar.class));
    }

    /**
     * Test Case 11: Save kamar dengan title null - auto generate
     * Path: saveKamar -> title null -> generate from nomor kamar
     */
    @Test
    void testSaveKamar_NullTitle_AutoGenerate() {
        kamar kamarWithoutTitle = new kamar();
        kamarWithoutTitle.setNomorKamar("105");
        kamarWithoutTitle.setHargaBulanan(1500000.0);
        kamarWithoutTitle.setTitle(null);

        when(kamarRepository.save(any(kamar.class))).thenAnswer(i -> i.getArgument(0));
        when(kamarRepository.existsByNomorKamar("105")).thenReturn(false);

        kamar result = kamarService.saveKamar(kamarWithoutTitle);

        assertEquals("Kamar 105", result.getTitle());
        verify(kamarRepository, times(1)).save(any(kamar.class));
    }

    /**
     * Test Case 12: Save kamar dengan description null - auto generate
     * Path: saveKamar -> description null -> generate from nomor kamar
     */
    @Test
    void testSaveKamar_NullDescription_AutoGenerate() {
        kamar kamarWithoutDesc = new kamar();
        kamarWithoutDesc.setNomorKamar("106");
        kamarWithoutDesc.setHargaBulanan(1500000.0);
        kamarWithoutDesc.setDescription(null);

        when(kamarRepository.save(any(kamar.class))).thenAnswer(i -> i.getArgument(0));
        when(kamarRepository.existsByNomorKamar("106")).thenReturn(false);

        kamar result = kamarService.saveKamar(kamarWithoutDesc);

        assertEquals("Kamar kost nomor 106", result.getDescription());
        verify(kamarRepository, times(1)).save(any(kamar.class));
    }

    /**
     * Test Case 13: Save kamar dengan price null - copy dari hargaBulanan
     * Path: saveKamar -> price null -> set from hargaBulanan
     */
    @Test
    void testSaveKamar_NullPrice_CopyFromHargaBulanan() {
        kamar kamarWithoutPrice = new kamar();
        kamarWithoutPrice.setNomorKamar("107");
        kamarWithoutPrice.setHargaBulanan(2000000.0);
        kamarWithoutPrice.setPrice(null);

        when(kamarRepository.save(any(kamar.class))).thenAnswer(i -> i.getArgument(0));
        when(kamarRepository.existsByNomorKamar("107")).thenReturn(false);

        kamar result = kamarService.saveKamar(kamarWithoutPrice);

        assertEquals(2000000.0, result.getPrice());
        verify(kamarRepository, times(1)).save(any(kamar.class));
    }

    /**
     * Test Case 14: Save kamar baru dengan nomor yang sudah ada
     * Path: saveKamar -> id null & nomor exists -> exception
     */
    @Test
    void testSaveKamar_DuplicateNomorKamar() {
        kamar newKamar = new kamar();
        newKamar.setId(null); // Kamar baru
        newKamar.setNomorKamar("101");
        newKamar.setHargaBulanan(1500000.0);

        when(kamarRepository.existsByNomorKamar("101")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            kamarService.saveKamar(newKamar);
        });

        assertEquals("Nomor kamar 101 sudah ada", exception.getMessage());
        verify(kamarRepository, never()).save(any());
    }

    /**
     * Test Case 15: Update kamar existing (id tidak null)
     * Path: saveKamar -> id not null -> skip duplicate check -> save
     */
    @Test
    void testSaveKamar_UpdateExisting() {
        validKamar.setId(1L); // Existing kamar
        validKamar.setHargaBulanan(2000000.0); // Update harga

        when(kamarRepository.save(any(kamar.class))).thenReturn(validKamar);

        kamar result = kamarService.saveKamar(validKamar);

        assertEquals(2000000.0, result.getHargaBulanan());
        verify(kamarRepository, times(1)).save(validKamar);
        verify(kamarRepository, never()).existsByNomorKamar(any()); // No duplicate check for update
    }

    /**
     * Test Case 16: Delete kamar by ID
     * Path: deleteKamar -> repository.deleteById
     */
    @Test
    void testDeleteKamar() {
        doNothing().when(kamarRepository).deleteById(1L);

        assertDoesNotThrow(() -> kamarService.deleteKamar(1L));

        verify(kamarRepository, times(1)).deleteById(1L);
    }

    /**
     * Test Case 17: Check nomor kamar exists - true
     * Path: nomorKamarExists -> true
     */
    @Test
    void testNomorKamarExists_True() {
        when(kamarRepository.existsByNomorKamar("101")).thenReturn(true);

        boolean exists = kamarService.nomorKamarExists("101");

        assertTrue(exists);
        verify(kamarRepository, times(1)).existsByNomorKamar("101");
    }

    /**
     * Test Case 18: Check nomor kamar exists - false
     * Path: nomorKamarExists -> false
     */
    @Test
    void testNomorKamarExists_False() {
        when(kamarRepository.existsByNomorKamar("999")).thenReturn(false);

        boolean exists = kamarService.nomorKamarExists("999");

        assertFalse(exists);
        verify(kamarRepository, times(1)).existsByNomorKamar("999");
    }

    /**
     * Test Case 19: Boundary test - harga minimum valid (0.01)
     */
    @Test
    void testSaveKamar_MinimumValidPrice() {
        kamar kamarMinPrice = new kamar();
        kamarMinPrice.setNomorKamar("108");
        kamarMinPrice.setHargaBulanan(0.01);

        when(kamarRepository.save(any(kamar.class))).thenAnswer(i -> i.getArgument(0));
        when(kamarRepository.existsByNomorKamar("108")).thenReturn(false);

        kamar result = kamarService.saveKamar(kamarMinPrice);

        assertEquals(0.01, result.getHargaBulanan());
        verify(kamarRepository, times(1)).save(any(kamar.class));
    }

    /**
     * Test Case 20: Integration path - save dengan semua default values
     * Path: saveKamar dengan minimal input -> semua default di-set -> save
     */
    @Test
    void testSaveKamar_AllDefaultsSet() {
        kamar minimalKamar = new kamar();
        minimalKamar.setNomorKamar("109");
        minimalKamar.setHargaBulanan(1500000.0);
        // Semua field lain null

        when(kamarRepository.save(any(kamar.class))).thenAnswer(i -> i.getArgument(0));
        when(kamarRepository.existsByNomorKamar("109")).thenReturn(false);

        kamar result = kamarService.saveKamar(minimalKamar);

        // Verify all defaults are set
        assertEquals("kosong", result.getStatus());
        assertEquals("", result.getFasilitas());
        assertEquals("Kamar 109", result.getTitle());
        assertEquals("Kamar kost nomor 109", result.getDescription());
        assertEquals(1500000.0, result.getPrice());
        
        verify(kamarRepository, times(1)).save(any(kamar.class));
    }
}
