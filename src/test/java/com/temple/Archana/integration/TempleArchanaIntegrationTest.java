
package com.temple.Archana.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.temple.Archana.model.DevotesNames;
import com.temple.Archana.repository.DevoteeRepository;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:temple_archana_it;DB_CLOSE_DELAY=-1",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
@AutoConfigureMockMvc
@Transactional
class TempleArchanaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DevoteeRepository devoteeRepository;

    @Test
    void createDevoteeThroughApi_persistsRecord()
            throws Exception {

        // Arrange: prepare an actual HTTP request
        String requestJson = """
            {
                "gotram": "Kowshika",
                "nakshathram": "Rohini",
                "raashi": "Mithuna",
                "age": 25,
                "email": "integration-flow@example.com",
                "amount": 300.0
            }
            """;

        // Act: send the request through the real controller
        mockMvc.perform(
                post("/devotees/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson)
        )
        // Assert: verify the HTTP response
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.gotram").value("Kowshika"))
        .andExpect(jsonPath("$.age").value(25));

        // Verify: retrieve the saved record from the real repository
        var savedDevotees =
                devoteeRepository.findByEmail(
                        "integration-flow@example.com");

        assertFalse(savedDevotees.isEmpty());

        DevotesNames saved = savedDevotees.get(0);

        assertEquals("Kowshika", saved.getGotram());
        assertEquals("Rohini", saved.getNakshathram());
        assertEquals(25, saved.getAge());
        assertEquals(300.0, saved.getAmount());
    }

@Test
void createDevoteeWithInvalidAge_returnsBadRequestAndDoesNotPersist()
        throws Exception {

    String email = "invalid-age-it@example.com";

    String requestJson = """
        {
            "gotram": "Kowshika",
            "nakshathram": "Rohini",
            "raashi": "Mithuna",
            "age": 15,
            "email": "invalid-age-it@example.com",
            "amount": 300.0
        }
        """;

    // Act: send an invalid request through the real controller
    mockMvc.perform(
            post("/devotees/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson)
    )
    .andExpect(status().isBadRequest());

    // Assert: verify no record was persisted
    assertTrue(
            devoteeRepository.findByEmail(email).isEmpty(),
            "Invalid request must not create a database record"
    );
}


@Test
void updateDevoteeById_updatesDatabaseRecord() throws Exception {

    // Arrange: create an existing devotee in H2
    DevotesNames devotee = new DevotesNames();
    devotee.setGotram("Kowshika");
    devotee.setNakshathram("Rohini");
    devotee.setRaashi("Mithuna");
    devotee.setAge(25);
    devotee.setEmail("update-before-it@example.com");
    devotee.setAmount(300.0);

    DevotesNames saved =
            devoteeRepository.saveAndFlush(devotee);

    Long id = saved.getId();

    // Act: send a PUT request to update this devotee
    String updatedJson = """
        {
            "gotram": "Vashishta",
            "nakshathram": "Ashwini",
            "raashi": "Mesha",
            "age": 30,
            "email": "update-after-it@example.com",
            "amount": 450.0
        }
        """;

    mockMvc.perform(
            put("/updateStudents/id/" + id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(updatedJson)
    )
    .andExpect(status().isOk());

    // Assert: retrieve the record from the real repository
    DevotesNames updated = devoteeRepository.findById(id)
            .orElseThrow();

    assertEquals("Vashishta", updated.getGotram());
    assertEquals("Ashwini", updated.getNakshathram());
    assertEquals("Mesha", updated.getRaashi());
    assertEquals(30, updated.getAge());
    assertEquals("update-after-it@example.com", updated.getEmail());
    assertEquals(450.0, updated.getAmount());
}


@Test
void deleteDevoteeById_removesRecordFromDatabase() throws Exception {

    // Arrange: save a devotee in the H2 database
    DevotesNames devotee = new DevotesNames();
    devotee.setGotram("Kowshika");
    devotee.setNakshathram("Rohini");
    devotee.setRaashi("Mithuna");
    devotee.setAge(25);
    devotee.setEmail("delete-test@example.com");
    devotee.setAmount(300.0);

    DevotesNames saved =
            devoteeRepository.saveAndFlush(devotee);

    Long id = saved.getId();

    // Confirm the record exists before deleting
    assertTrue(devoteeRepository.findById(id).isPresent());

    // Act: call the actual DELETE endpoint
    mockMvc.perform(
            delete("/deleteStudent/id/" + id)
    )
    .andExpect(status().isOk());

    // Assert: confirm the record no longer exists
    assertFalse(
            devoteeRepository.findById(id).isPresent(),
            "The devotee should have been deleted"
    );
}

}
