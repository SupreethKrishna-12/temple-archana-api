
package com.temple.Archana.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.temple.Archana.dto.CreateDevoteeRequestDTO;
import com.temple.Archana.dto.DevoteeResponseDTO;
import com.temple.Archana.model.DevotesNames;
import com.temple.Archana.service.PoojaService;

@WebMvcTest(PoojaApi.class)
class PoojaApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PoojaService poojaService;

    private DevotesNames sampleDevotee() {
        DevotesNames d = new DevotesNames();
        d.setId(1L);
        d.setGotram("Kowshika");
        d.setNakshathram("Rohini");
        d.setRaashi("Mithuna");
        d.setAge(25);
        d.setEmail("test@example.com");
        d.setAmount(300.0);
        return d;
    }

    private String validDevoteeJson() {
        return """
            {
                "gotram": "Kowshika",
                "nakshathram": "Rohini",
                "raashi": "Mithuna",
                "age": 25,
                "email": "test@example.com",
                "amount": 300.0
            }
            """;
    }

    // ---------- READ ALL ----------

    @Test
    void getAllDetails_returnsOk() throws Exception {
        when(poojaService.getAllDetails())
                .thenReturn(List.of(sampleDevotee()));

        mockMvc.perform(get("/allStudents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].gotram").value("Kowshika"));

        verify(poojaService).getAllDetails();
    }

    @Test
    void getDevotees_returnsPage() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<DevotesNames> page = new PageImpl<>(
                List.of(sampleDevotee()), pageable, 1);

        when(poojaService.getAllDevotees(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/devotees")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(poojaService).getAllDevotees(any(Pageable.class));
    }

    @Test
    void getAllDevoteesAsDTO_returnsDtoList() throws Exception {
        DevoteeResponseDTO dto = new DevoteeResponseDTO(
                1L, "Kowshika", "Rohini", "Mithuna", 25);

        when(poojaService.getAllDevoteesAsDTO())
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/devotees/dto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].gotram").value("Kowshika"))
                .andExpect(jsonPath("$[0].age").value(25))
                .andExpect(jsonPath("$[0].email").doesNotExist());

        verify(poojaService).getAllDevoteesAsDTO();
    }

    // ---------- BASIC SEARCHES ----------

    @Test
    void basicSearchRoutes_returnOk() throws Exception {
        DevotesNames devotee = sampleDevotee();
        List<DevotesNames> rows = List.of(devotee);

        when(poojaService.getByGotram("Kowshika")).thenReturn(rows);
        when(poojaService.getByNakshathram("Rohini")).thenReturn(rows);
        when(poojaService.getDevoteeByEmail("test@example.com"))
                .thenReturn(rows);
        when(poojaService.getDevoteeById(1L)).thenReturn(devotee);

        mockMvc.perform(get("/allStudents/Kowshika"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/allStudents/nakshatram/Rohini"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/allStudents/email-search/test@example.com"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/allStudents/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(poojaService).getByGotram("Kowshika");
        verify(poojaService).getByNakshathram("Rohini");
        verify(poojaService).getDevoteeByEmail("test@example.com");
        verify(poojaService).getDevoteeById(1L);
    }

    // ---------- AGE SEARCHES ----------

    @Test
    void ageSearchRoutes_returnOk() throws Exception {
        List<DevotesNames> rows = List.of(sampleDevotee());

        when(poojaService.getByAgeGreaterThan(20)).thenReturn(rows);
        when(poojaService.getOlderThanUsingQuery(20)).thenReturn(rows);
        when(poojaService.getByAgeBetween(18, 30)).thenReturn(rows);
        when(poojaService.getByAgeBetweenUsingQuery(18, 30))
                .thenReturn(rows);

        mockMvc.perform(get("/allStudents/age/20"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/allStudents/age/query/20"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/allStudents/ageBetween/18/30"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/allStudents/ageBetweenQuery/18/30"))
                .andExpect(status().isOk());

        verify(poojaService).getByAgeGreaterThan(20);
        verify(poojaService).getOlderThanUsingQuery(20);
        verify(poojaService).getByAgeBetween(18, 30);
        verify(poojaService).getByAgeBetweenUsingQuery(18, 30);
    }

    // ---------- GOTRAM / RAASHI SEARCHES ----------

    @Test
    void combinedSearchRoutes_returnOk() throws Exception {
        List<DevotesNames> rows = List.of(sampleDevotee());

        when(poojaService.getByGotramAndRaashi("Kowshika", "Mithuna"))
                .thenReturn(rows);
        when(poojaService.getByGotramAndRaashiUsingQuery(
                "Kowshika", "Mithuna")).thenReturn(rows);
        when(poojaService.getByGotramOrNakshathram(
                "Kowshika", "Rohini")).thenReturn(rows);
        when(poojaService.getByGotramOrNakshathramUsingQuery(
                "Kowshika", "Rohini")).thenReturn(rows);

        mockMvc.perform(get("/allStudents/GR/Kowshika/Mithuna"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/allStudents/GRquery/Kowshika/Mithuna"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/allStudents/GON/Kowshika,Rohini"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/allStudents/GONQuery/Kowshika/Rohini"))
                .andExpect(status().isOk());

        verify(poojaService).getByGotramAndRaashi("Kowshika", "Mithuna");
        verify(poojaService).getByGotramAndRaashiUsingQuery(
                "Kowshika", "Mithuna");
        verify(poojaService).getByGotramOrNakshathram(
                "Kowshika", "Rohini");
        verify(poojaService).getByGotramOrNakshathramUsingQuery(
                "Kowshika", "Rohini");
    }

    // ---------- GOTRAM TEXT SEARCHES ----------

    @Test
    void gotramTextSearchRoutes_returnOk() throws Exception {
        List<DevotesNames> rows = List.of(sampleDevotee());

        when(poojaService.findByGotramContaining("shi")).thenReturn(rows);
        when(poojaService.findByGotramStartingWith("Kow")).thenReturn(rows);
        when(poojaService.findByGotramEndingWith("shika")).thenReturn(rows);
        when(poojaService.findByGotramContainingIgnoreCase("KOW"))
                .thenReturn(rows);

        mockMvc.perform(get("/allStudents/GotramContaining/shi"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/allStudents/GotramStartingWith/Kow"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/allStudents/GotramEndingWith/shika"))
                .andExpect(status().isOk());

        // Keep the route spelling from the existing controller.
        mockMvc.perform(get("/allStudents/GotramContaingIgnoreCase/KOW"))
                .andExpect(status().isOk());

        verify(poojaService).findByGotramContaining("shi");
        verify(poojaService).findByGotramStartingWith("Kow");
        verify(poojaService).findByGotramEndingWith("shika");
        verify(poojaService).findByGotramContainingIgnoreCase("KOW");
    }

    // ---------- EMAIL EXISTS AND GOTRAM COUNT ----------

    @Test
    void emailExists_returnsBoolean() throws Exception {
        when(poojaService.isEmailExists("test@example.com"))
                .thenReturn(true);

        mockMvc.perform(get("/allStudents/EmailExists/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(poojaService).isEmailExists("test@example.com");
    }

    @Test
    void countByGotram_returnsCount() throws Exception {
        when(poojaService.countByGotram("Kowshika")).thenReturn(3L);

        mockMvc.perform(get("/allStudents/countByGotram/Kowshika"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));

        verify(poojaService).countByGotram("Kowshika");
    }

    // ---------- PAGINATION AND FILTERING ----------

    @Test
    void getDevoteesByAgeBetween_returnsOk() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        DevoteeResponseDTO dto = new DevoteeResponseDTO(
                1L, "Kowshika", "Rohini", "Mithuna", 25);

        Page<DevoteeResponseDTO> page =
                new PageImpl<>(List.of(dto), pageable, 1);

        when(poojaService.getDevoteesByAgeBetween(
                eq(18), eq(40), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/devotees/by-age")
                        .param("minage", "18")
                        .param("maxage", "40")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(poojaService).getDevoteesByAgeBetween(
                eq(18), eq(40), any(Pageable.class));
    }

    // ---------- CREATE DTO ----------

    @Test
    void createDevotee_whenValid_returnsCreated() throws Exception {
        DevoteeResponseDTO response = new DevoteeResponseDTO(
                101L, "Kowshika", "Rohini", "Mithuna", 25);

        when(poojaService.createDevotee(any(CreateDevoteeRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/devotees/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validDevoteeJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(101))
                .andExpect(jsonPath("$.gotram").value("Kowshika"))
                .andExpect(jsonPath("$.age").value(25))
                .andExpect(jsonPath("$.email").doesNotExist())
                .andExpect(jsonPath("$.amount").doesNotExist());

        verify(poojaService).createDevotee(
                any(CreateDevoteeRequestDTO.class));
    }

    @Test
    void createDevotee_whenAgeInvalid_returnsBadRequest() throws Exception {
        String invalidJson = """
            {
                "gotram": "Kowshika",
                "nakshathram": "Rohini",
                "raashi": "Mithuna",
                "age": 15,
                "email": "invalid@example.com",
                "amount": 300.0
            }
            """;

        mockMvc.perform(post("/devotees/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        verify(poojaService, never())
                .createDevotee(any(CreateDevoteeRequestDTO.class));
    }

    // ---------- LEGACY CREATE ENDPOINTS ----------

    @Test
    void legacyCreateRoutes_returnCreated() throws Exception {
        when(poojaService.saveDevotee(any(DevotesNames.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/addStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validDevoteeJson()))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/createStudents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validDevoteeJson()))
                .andExpect(status().isCreated());

        verify(poojaService, times(2))
                .saveDevotee(any(DevotesNames.class));
    }

    // ---------- UPDATE ----------

    @Test
    void updateById_returnsOk() throws Exception {
        DevotesNames devotee = sampleDevotee();

        when(poojaService.updateDevoteeById(
                any(DevotesNames.class), eq(1L)))
                .thenReturn(devotee);

        mockMvc.perform(put("/updateStudents/id/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validDevoteeJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(poojaService).updateDevoteeById(
                any(DevotesNames.class), eq(1L));
    }

    @Test
    void updateByRaashi_returnsList() throws Exception {
        DevotesNames devotee = sampleDevotee();

        when(poojaService.updateDevoteeByRaashi(
                any(DevotesNames.class), eq("Mithuna")))
                .thenReturn(devotee);
        when(poojaService.getAllDetails())
                .thenReturn(List.of(devotee));

        mockMvc.perform(put("/updateStudents/Mithuna")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validDevoteeJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].gotram").value("Kowshika"));

        verify(poojaService).updateDevoteeByRaashi(
                any(DevotesNames.class), eq("Mithuna"));
        verify(poojaService).getAllDetails();
    }

    // ---------- DELETE ----------

    @Test
    void deleteById_returnsOk() throws Exception {
        when(poojaService.deleteDevoteeById(1L))
                .thenReturn("Deleted devotee with id 1 successfully");

        mockMvc.perform(delete("/deleteStudent/id/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "Deleted devotee with id 1 successfully"));

        verify(poojaService).deleteDevoteeById(1L);
    }

    @Test
    void deleteByGotram_whenFound_returnsOk() throws Exception {
        when(poojaService.deleteDevoteeByGotram("Kowshika"))
                .thenReturn("Deleted Successfully");

        mockMvc.perform(delete("/deleteStudents/Kowshika"))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted Successfully"));

        verify(poojaService).deleteDevoteeByGotram("Kowshika");
    }

    @Test
    void deleteByGotram_whenMissing_returnsNotFound() throws Exception {
        when(poojaService.deleteDevoteeByGotram("Unknown"))
                .thenReturn("Devotee Not Found");

        mockMvc.perform(delete("/deleteStudents/Unknown"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Devotee Not Found"));

        verify(poojaService).deleteDevoteeByGotram("Unknown");
    }
}
