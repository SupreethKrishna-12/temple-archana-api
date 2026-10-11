
package com.temple.Archana.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.temple.Archana.exception.DevoteeNotFoundException;
import com.temple.Archana.model.DevotesNames;
import com.temple.Archana.model.Pooja;
import com.temple.Archana.repository.DevoteeRepository;
import com.temple.Archana.repository.PoojaRepository;

@ExtendWith(MockitoExtension.class)
class PoojaServiceRelationshipTest {

    @Mock
    private PoojaRepository poojaRepository;

    @Mock
    private DevoteeRepository devoteeRepository;

    @InjectMocks
    private PoojaServiceRelationship relationshipService;

    private DevotesNames sampleDevotee(Long id) {
        DevotesNames devotee = new DevotesNames();
        devotee.setId(id);
        devotee.setGotram("Kowshika");
        devotee.setNakshathram("Rohini");
        devotee.setRaashi("Mithuna");
        devotee.setAge(25);
        devotee.setEmail("relationship-test@example.com");
        devotee.setAmount(300.0);
        return devotee;
    }

    // ---------- CREATE POOJA ----------

    @Test
    void createPooja_whenDevoteeExists_savesPooja() {
        DevotesNames devotee = sampleDevotee(1L);

        when(devoteeRepository.findById(1L))
                .thenReturn(Optional.of(devotee));

        when(poojaRepository.save(any(Pooja.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Pooja result = relationshipService.createPooja(
                "Archana", 500.0, 1L);

        assertNotNull(result);
        assertEquals("Archana", result.getPoojaName());
        assertEquals(500.0, result.getAmount());
        assertSame(devotee, result.getDevotee());

        verify(devoteeRepository).findById(1L);
        verify(poojaRepository).save(any(Pooja.class));
    }

    @Test
    void createPooja_whenDevoteeMissing_throwsException() {
        when(devoteeRepository.findById(999L))
                .thenReturn(Optional.empty());

        DevoteeNotFoundException exception = assertThrows(
                DevoteeNotFoundException.class,
                () -> relationshipService.createPooja(
                        "Archana", 500.0, 999L));

        assertEquals("Devotee with id 999 not found",
                exception.getMessage());

        verify(poojaRepository, never()).save(any(Pooja.class));
    }

    // ---------- GET DEVOTEE WITH POOJAS ----------

    @Test
    void getDevoteewithPoojas_whenDevoteeExists_returnsDevotee() {
        DevotesNames devotee = mock(DevotesNames.class);
        Pooja pooja = mock(Pooja.class);

        when(devoteeRepository.findById(1L))
                .thenReturn(Optional.of(devotee));

        when(devotee.getPoojas()).thenReturn(List.of(pooja));

        DevotesNames result =
                relationshipService.getDevoteewithPoojas(1L);

        assertSame(devotee, result);
        verify(devotee).getPoojas();
        verify(devoteeRepository).findById(1L);
    }

    @Test
    void getDevoteewithPoojas_whenDevoteeMissing_throwsException() {
        when(devoteeRepository.findById(999L))
                .thenReturn(Optional.empty());

        DevoteeNotFoundException exception = assertThrows(
                DevoteeNotFoundException.class,
                () -> relationshipService.getDevoteewithPoojas(999L));

        assertEquals("Devotee with id 999 not found",
                exception.getMessage());
    }

    // ---------- DIRTY CHECKING METHOD ----------

    @Test
    void testdirtychecking_whenDevoteeExists_updatesAgeTo40() {
        DevotesNames devotee = sampleDevotee(1L);

        when(devoteeRepository.findById(1L))
                .thenReturn(Optional.of(devotee));

        relationshipService.testdirtychecking(1L);

        assertEquals(40, devotee.getAge());
        verify(devoteeRepository).findById(1L);
    }

    @Test
    void testdirtychecking_whenDevoteeMissing_throwsException() {
        when(devoteeRepository.findById(999L))
                .thenReturn(Optional.empty());

        DevoteeNotFoundException exception = assertThrows(
                DevoteeNotFoundException.class,
                () -> relationshipService.testdirtychecking(999L));

        assertEquals("Devotee with id 999 Not found",
                exception.getMessage());
    }
}
