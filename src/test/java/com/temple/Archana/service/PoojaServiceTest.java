
package com.temple.Archana.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.temple.Archana.dto.CreateDevoteeRequestDTO;
import com.temple.Archana.dto.DevoteeResponseDTO;
import com.temple.Archana.exception.DevoteeNotFoundException;
import com.temple.Archana.model.DevotesNames;
import com.temple.Archana.repository.DevoteeRepository;

@ExtendWith(MockitoExtension.class)
class PoojaServiceTest {

    @Mock
    private DevoteeRepository devoteeRepository;

    @InjectMocks
    private PoojaService poojaService;

    private DevotesNames sampleDevotee(
            Long id, String gotram, String nakshathram,
            String raashi, int age) {

        DevotesNames d = new DevotesNames();
        d.setId(id);
        d.setGotram(gotram);
        d.setNakshathram(nakshathram);
        d.setRaashi(raashi);
        d.setAge(age);
        d.setEmail("test@example.com");
        d.setAmount(300.0);
        return d;
    }

    // ---------- READ ALL ----------

    @Test
    void getAllDetails_returnsRepositoryResults() {
        List<DevotesNames> expected =
                List.of(sampleDevotee(1L, "Kowshika", "Rohini",
                        "Mithuna", 25));

        when(devoteeRepository.findAll()).thenReturn(expected);

        assertSame(expected, poojaService.getAllDetails());
        verify(devoteeRepository).findAll();
    }

    @Test
    void getAllDevotees_returnsPageFromRepository() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<DevotesNames> expected = new PageImpl<>(
                List.of(sampleDevotee(1L, "Kowshika", "Rohini",
                        "Mithuna", 25)),
                pageable, 1);

        when(devoteeRepository.findAll(pageable))
                .thenReturn(expected);

        assertSame(expected, poojaService.getAllDevotees(pageable));
        verify(devoteeRepository).findAll(pageable);
    }

    @Test
    void getAllDevoteesAsDTO_mapsEntityFields() {
        DevotesNames devotee = sampleDevotee(
                1L, "Kowshika", "Rohini", "Mithuna", 25);

        when(devoteeRepository.findAll())
                .thenReturn(List.of(devotee));

        List<DevoteeResponseDTO> result =
                poojaService.getAllDevoteesAsDTO();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Kowshika", result.get(0).getGotram());
        assertEquals("Rohini", result.get(0).getNakshathram());
        assertEquals("Mithuna", result.get(0).getRaashi());
        assertEquals(25, result.get(0).getAge());
    }

    // ---------- BASIC SEARCHES ----------

    @Test
    void basicSearchMethods_delegateToRepository() {
        DevotesNames devotee = sampleDevotee(
                1L, "Kowshika", "Rohini", "Mithuna", 25);
        List<DevotesNames> rows = List.of(devotee);

        when(devoteeRepository.findByGotram("Kowshika"))
                .thenReturn(rows);
        when(devoteeRepository.findByNakshathram("Rohini"))
                .thenReturn(rows);
        when(devoteeRepository.findByEmail("test@example.com"))
                .thenReturn(rows);

        assertSame(rows, poojaService.getByGotram("Kowshika"));
        assertSame(rows, poojaService.getByNakshathram("Rohini"));
        assertSame(rows,
                poojaService.getDevoteeByEmail("test@example.com"));

        verify(devoteeRepository).findByGotram("Kowshika");
        verify(devoteeRepository).findByNakshathram("Rohini");
        verify(devoteeRepository).findByEmail("test@example.com");
    }

    @Test
    void getDevoteeById_whenFound_returnsDevotee() {
        DevotesNames expected = sampleDevotee(
                1L, "Kowshika", "Rohini", "Mithuna", 25);

        when(devoteeRepository.findById(1L))
                .thenReturn(Optional.of(expected));

        assertSame(expected, poojaService.getDevoteeById(1L));
        verify(devoteeRepository).findById(1L);
    }

    @Test
    void getDevoteeById_whenMissing_throwsException() {
        when(devoteeRepository.findById(999L))
                .thenReturn(Optional.empty());

        DevoteeNotFoundException exception = assertThrows(
                DevoteeNotFoundException.class,
                () -> poojaService.getDevoteeById(999L));

        assertEquals("Devotee with id 999 not found",
                exception.getMessage());
    }

    // ---------- AGE QUERIES ----------

    @Test
    void ageQueryMethods_delegateToRepository() {
        List<DevotesNames> rows = List.of(
                sampleDevotee(1L, "Kowshika", "Rohini",
                        "Mithuna", 25));

        when(devoteeRepository.findByAgeGreaterThan(18))
                .thenReturn(rows);
        when(devoteeRepository.findOlderThanUsingQuery(18))
                .thenReturn(rows);
        when(devoteeRepository.findByAgeBetween(20, 30))
                .thenReturn(rows);
        when(devoteeRepository.findByAgeBetweenUsingQuery(20, 30))
                .thenReturn(rows);

        assertSame(rows, poojaService.getByAgeGreaterThan(18));
        assertSame(rows, poojaService.getOlderThanUsingQuery(18));
        assertSame(rows, poojaService.getByAgeBetween(20, 30));
        assertSame(rows,
                poojaService.getByAgeBetweenUsingQuery(20, 30));

        verify(devoteeRepository).findByAgeGreaterThan(18);
        verify(devoteeRepository).findOlderThanUsingQuery(18);
        verify(devoteeRepository).findByAgeBetween(20, 30);
        verify(devoteeRepository).findByAgeBetweenUsingQuery(20, 30);
    }

    // ---------- GOTRAM / RAASHI QUERIES ----------

    @Test
    void combinedSearchMethods_delegateToRepository() {
        List<DevotesNames> rows = List.of(
                sampleDevotee(1L, "Kowshika", "Rohini",
                        "Mithuna", 25));

        when(devoteeRepository.findByGotramAndRaashi(
                "Kowshika", "Mithuna")).thenReturn(rows);

        when(devoteeRepository.findByGotramAndRaashiUsingQuery(
                "Kowshika", "Mithuna")).thenReturn(rows);

        when(devoteeRepository.findByGotramOrNakshathram(
                "Kowshika", "Rohini")).thenReturn(rows);

        when(devoteeRepository.findByGotramOrNakshathramUsingQuery(
                "Kowshika", "Rohini")).thenReturn(rows);

        assertSame(rows,
                poojaService.getByGotramAndRaashi(
                        "Kowshika", "Mithuna"));

        assertSame(rows,
                poojaService.getByGotramAndRaashiUsingQuery(
                        "Kowshika", "Mithuna"));

        assertSame(rows,
                poojaService.getByGotramOrNakshathram(
                        "Kowshika", "Rohini"));

        assertSame(rows,
                poojaService.getByGotramOrNakshathramUsingQuery(
                        "Kowshika", "Rohini"));

        verify(devoteeRepository).findByGotramAndRaashi(
                "Kowshika", "Mithuna");
        verify(devoteeRepository).findByGotramAndRaashiUsingQuery(
                "Kowshika", "Mithuna");
        verify(devoteeRepository).findByGotramOrNakshathram(
                "Kowshika", "Rohini");
        verify(devoteeRepository).findByGotramOrNakshathramUsingQuery(
                "Kowshika", "Rohini");
    }

    // ---------- GOTRAM TEXT SEARCHES ----------

    @Test
    void gotramTextSearchMethods_delegateToRepository() {
        List<DevotesNames> rows = List.of(
                sampleDevotee(1L, "Kowshika", "Rohini",
                        "Mithuna", 25));

        when(devoteeRepository.findByGotramContaining("shi"))
                .thenReturn(rows);
        when(devoteeRepository.findByGotramStartingWith("Kow"))
                .thenReturn(rows);
        when(devoteeRepository.findByGotramEndingWith("shika"))
                .thenReturn(rows);
        when(devoteeRepository.findByGotramContainingIgnoreCase("KOW"))
                .thenReturn(rows);

        assertSame(rows, poojaService.findByGotramContaining("shi"));
        assertSame(rows, poojaService.findByGotramStartingWith("Kow"));
        assertSame(rows, poojaService.findByGotramEndingWith("shika"));
        assertSame(rows,
                poojaService.findByGotramContainingIgnoreCase("KOW"));

        verify(devoteeRepository).findByGotramContaining("shi");
        verify(devoteeRepository).findByGotramStartingWith("Kow");
        verify(devoteeRepository).findByGotramEndingWith("shika");
        verify(devoteeRepository)
                .findByGotramContainingIgnoreCase("KOW");
    }

    // ---------- EXISTENCE AND COUNT ----------

    @Test
    void isEmailExists_whenEmailExists_returnsTrue() {
        when(devoteeRepository.existsByEmail("test@example.com"))
                .thenReturn(true);

        assertTrue(poojaService.isEmailExists("test@example.com"));
        verify(devoteeRepository).existsByEmail("test@example.com");
    }

    @Test
    void isEmailExists_whenEmailDoesNotExist_returnsFalse() {
        when(devoteeRepository.existsByEmail("missing@example.com"))
                .thenReturn(false);

        assertFalse(poojaService.isEmailExists("missing@example.com"));
    }

    @Test
    void countByGotram_returnsRepositoryCount() {
        when(devoteeRepository.countByGotram("Kowshika"))
                .thenReturn(3L);

        assertEquals(3L, poojaService.countByGotram("Kowshika"));
        verify(devoteeRepository).countByGotram("Kowshika");
    }

    // ---------- AGE FILTER + PAGINATION + DTO ----------

    @Test
    void getDevoteesByAgeBetween_returnsPaginatedDTOs() {
        Pageable pageable = PageRequest.of(0, 10);

        DevotesNames devotee = sampleDevotee(
                10L, "Kowshika", "Rohini", "Mithuna", 25);

        Page<DevotesNames> entityPage =
                new PageImpl<>(List.of(devotee), pageable, 1);

        when(devoteeRepository.findByAgeBetween(
                18, 40, pageable)).thenReturn(entityPage);

        Page<DevoteeResponseDTO> result =
                poojaService.getDevoteesByAgeBetween(
                        18, 40, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getContent().size());
        assertEquals(10L, result.getContent().get(0).getId());
        assertEquals("Kowshika",
                result.getContent().get(0).getGotram());
        assertEquals(25, result.getContent().get(0).getAge());

        verify(devoteeRepository).findByAgeBetween(
                18, 40, pageable);
    }

    // ---------- CREATE ----------

    @Test
    void createDevotee_mapsRequestSavesAndReturnsDTO() {
        CreateDevoteeRequestDTO request =
                new CreateDevoteeRequestDTO(
                        "Kowshika", "Rohini", "Mithuna",
                        25, "newdevotee@example.com", 300.0);

        when(devoteeRepository.save(any(DevotesNames.class)))
                .thenAnswer(invocation -> {
                    DevotesNames saved = invocation.getArgument(0);
                    saved.setId(100L);
                    return saved;
                });

        DevoteeResponseDTO response =
                poojaService.createDevotee(request);

        assertEquals(100L, response.getId());
        assertEquals("Kowshika", response.getGotram());
        assertEquals("Rohini", response.getNakshathram());
        assertEquals("Mithuna", response.getRaashi());
        assertEquals(25, response.getAge());

        ArgumentCaptor<DevotesNames> captor =
                ArgumentCaptor.forClass(DevotesNames.class);
        verify(devoteeRepository).save(captor.capture());

        assertEquals("newdevotee@example.com",
                captor.getValue().getEmail());
        assertEquals(300.0, captor.getValue().getAmount());
    }

    @Test
    void saveDevotee_delegatesToRepository() {
        DevotesNames devotee = sampleDevotee(
                1L, "Kowshika", "Rohini", "Mithuna", 25);

        when(devoteeRepository.save(devotee)).thenReturn(devotee);

        assertSame(devotee, poojaService.saveDevotee(devotee));
        verify(devoteeRepository).save(devotee);
    }

    // ---------- UPDATE BY ID ----------

    @Test
    void updateDevoteeById_whenFound_updatesAllFields() {
        DevotesNames existing = sampleDevotee(
                1L, "Kowshika", "Rohini", "Mithuna", 25);

        DevotesNames request = sampleDevotee(
                null, "Vashishta", "Ashwini", "Mesha", 30);
        request.setEmail("updated@example.com");
        request.setAmount(450.0);

        when(devoteeRepository.findById(1L))
                .thenReturn(Optional.of(existing));
        when(devoteeRepository.save(existing)).thenReturn(existing);

        DevotesNames result =
                poojaService.updateDevoteeById(request, 1L);

        assertSame(existing, result);
        assertEquals("Vashishta", result.getGotram());
        assertEquals("Ashwini", result.getNakshathram());
        assertEquals("Mesha", result.getRaashi());
        assertEquals(30, result.getAge());
        assertEquals("updated@example.com", result.getEmail());
        assertEquals(450.0, result.getAmount());

        verify(devoteeRepository).save(existing);
    }

    @Test
    void updateDevoteeById_whenMissing_throwsExceptionWithoutSaving() {
        when(devoteeRepository.findById(999L))
                .thenReturn(Optional.empty());

        DevotesNames request = sampleDevotee(
                null, "Vashishta", "Ashwini", "Mesha", 30);

        assertThrows(DevoteeNotFoundException.class,
                () -> poojaService.updateDevoteeById(request, 999L));

        verify(devoteeRepository, never())
                .save(any(DevotesNames.class));
    }

    // ---------- UPDATE BY RAASHI ----------

    @Test
    void updateDevoteeByRaashi_whenFound_updatesAndSaves() {
        DevotesNames existing = sampleDevotee(
                1L, "Kowshika", "Rohini", "Mithuna", 25);

        DevotesNames request = sampleDevotee(
                null, "Vashishta", "Ashwini", "Mesha", 30);

        when(devoteeRepository.findFirstByRaashi("Mithuna"))
                .thenReturn(Optional.of(existing));
        when(devoteeRepository.save(existing)).thenReturn(existing);

        DevotesNames result =
                poojaService.updateDevoteeByRaashi(request, "Mithuna");

        assertSame(existing, result);
        assertEquals("Vashishta", result.getGotram());
        assertEquals("Ashwini", result.getNakshathram());
        assertEquals("Mesha", result.getRaashi());

        verify(devoteeRepository).save(existing);
    }

    @Test
    void updateDevoteeByRaashi_whenMissing_throwsExceptionWithoutSaving() {
        DevotesNames request = sampleDevotee(
                null, "Vashishta", "Ashwini", "Mesha", 30);

        when(devoteeRepository.findFirstByRaashi("Unknown"))
                .thenReturn(Optional.empty());

        DevoteeNotFoundException exception = assertThrows(
                DevoteeNotFoundException.class,
                () -> poojaService.updateDevoteeByRaashi(
                        request, "Unknown"));

        assertEquals("Devotee with Raashi Unknown not found",
                exception.getMessage());

        verify(devoteeRepository, never())
                .save(any(DevotesNames.class));
    }

    // ---------- DELETE BY ID ----------

    @Test
    void deleteDevoteeById_whenFound_deletesRecord() {
        DevotesNames devotee = sampleDevotee(
                1L, "Kowshika", "Rohini", "Mithuna", 25);

        when(devoteeRepository.findById(1L))
                .thenReturn(Optional.of(devotee));

        String result = poojaService.deleteDevoteeById(1L);

        assertEquals("Deleted devotee with id 1 successfully", result);
        verify(devoteeRepository).delete(devotee);
    }

    @Test
    void deleteDevoteeById_whenMissing_throwsExceptionWithoutDeleting() {
        when(devoteeRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(DevoteeNotFoundException.class,
                () -> poojaService.deleteDevoteeById(999L));

        verify(devoteeRepository, never())
                .delete(any(DevotesNames.class));
    }

    // ---------- DELETE BY GOTRAM ----------

    @Test
    void deleteDevoteeByGotram_whenMatchesExist_deletesFirstMatch() {
        DevotesNames first = sampleDevotee(
                1L, "Kowshika", "Rohini", "Mithuna", 25);
        DevotesNames second = sampleDevotee(
                2L, "Kowshika", "Ashwini", "Mesha", 30);

        when(devoteeRepository.findByGotram("Kowshika"))
                .thenReturn(List.of(first, second));

        assertEquals("Deleted Successfully",
                poojaService.deleteDevoteeByGotram("Kowshika"));

        verify(devoteeRepository).delete(first);
        verify(devoteeRepository, never()).delete(second);
    }

    @Test
    void deleteDevoteeByGotram_whenNoMatch_returnsNotFound() {
        when(devoteeRepository.findByGotram("Unknown"))
                .thenReturn(List.of());

        assertEquals("Devotee Not Found",
                poojaService.deleteDevoteeByGotram("Unknown"));

        verify(devoteeRepository, never())
                .delete(any(DevotesNames.class));
    }
}
