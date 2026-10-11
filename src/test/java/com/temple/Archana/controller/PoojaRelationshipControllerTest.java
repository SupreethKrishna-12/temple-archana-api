
package com.temple.Archana.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.temple.Archana.model.DevotesNames;
import com.temple.Archana.model.Pooja;
import com.temple.Archana.service.PoojaServiceRelationship;

@WebMvcTest(PoojaRelationshipController.class)
class PoojaRelationshipControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PoojaServiceRelationship poojaServiceRelationship;

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

    // ---------- CREATE POOJA ----------

    @Test
    void createPooja_returnsCreated() throws Exception {
        DevotesNames devotee = sampleDevotee();
        Pooja pooja = new Pooja("Archana", 500.0, devotee);

        when(poojaServiceRelationship.createPooja(
                "Archana", 500.0, 1L))
                .thenReturn(pooja);

        String requestJson = """
            {
                "poojaName": "Archana",
                "amount": 500.0
            }
            """;

        mockMvc.perform(post("/poojas/devotee/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.poojaName").value("Archana"))
                .andExpect(jsonPath("$.amount").value(500.0));

        verify(poojaServiceRelationship)
                .createPooja("Archana", 500.0, 1L);
    }

    // ---------- GET DEVOTEE WITH POOJAS ----------

    @Test
    void getDevoteeWithPoojas_returnsDevotee() throws Exception {
        DevotesNames devotee = sampleDevotee();

        when(poojaServiceRelationship.getDevoteewithPoojas(1L))
                .thenReturn(devotee);

        mockMvc.perform(get("/allStudents/withPoojas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.gotram").value("Kowshika"));

        verify(poojaServiceRelationship).getDevoteewithPoojas(1L);
    }

    // ---------- DIRTY CHECKING ----------

    @Test
    void testDirtyChecking_returnsOk() throws Exception {
        doNothing().when(poojaServiceRelationship)
                .testdirtychecking(1L);

        mockMvc.perform(put("/test-dirty/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Age updated to 35"));

        verify(poojaServiceRelationship).testdirtychecking(1L);
    }
}
