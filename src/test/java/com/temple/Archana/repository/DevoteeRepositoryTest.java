
package com.temple.Archana.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.temple.Archana.model.DevotesNames;

@DataJpaTest
class DevoteeRepositoryTest {

    @Autowired
    private DevoteeRepository devoteeRepository;

    @Test
    void saveAndFindById_returnsSavedDevotee() {

        // 1. Arrange: create a new devotee
        DevotesNames devotee = new DevotesNames();
        devotee.setGotram("TestGotram");
        devotee.setNakshathram("Rohini");
        devotee.setRaashi("Mithuna");
        devotee.setAge(25);
        devotee.setEmail("integration-test@example.com");
        devotee.setAmount(300.0);

        // 2. Act: save the entity to the test database
        DevotesNames saved = devoteeRepository.save(devotee);

        // 3. Verify: database-generated ID exists
        assertNotNull(saved.getId());

        // 4. Retrieve the record using its generated ID
        DevotesNames found = devoteeRepository
                .findById(saved.getId())
                .orElseThrow();

        // 5. Assert: verify the retrieved database values
        assertEquals("TestGotram", found.getGotram());
        assertEquals("Rohini", found.getNakshathram());
        assertEquals("Mithuna", found.getRaashi());
        assertEquals(25, found.getAge());
        assertEquals("integration-test@example.com", found.getEmail());
        assertEquals(300.0, found.getAmount());
    }
    

@Test
void findByGotram_returnsOnlyMatchingDevotees() {

    // Arrange: create the first devotee
    DevotesNames first = new DevotesNames();
    first.setGotram("Kowshika");
    first.setNakshathram("Rohini");
    first.setRaashi("Mithuna");
    first.setAge(25);
    first.setEmail("kowshika-test@example.com");
    first.setAmount(300.0);

    // Arrange: create a second devotee with a different Gotram
    DevotesNames second = new DevotesNames();
    second.setGotram("Vashishta");
    second.setNakshathram("Ashwini");
    second.setRaashi("Mesha");
    second.setAge(30);
    second.setEmail("vashishta-test@example.com");
    second.setAmount(400.0);

    // Act: save both records to the test database
    devoteeRepository.saveAndFlush(first);
    devoteeRepository.saveAndFlush(second);

    // Execute the actual derived query
    List<DevotesNames> results =
            devoteeRepository.findByGotram("Kowshika");

    // Assert: only the matching record should be returned
    assertEquals(1, results.size());
    assertEquals("Kowshika", results.get(0).getGotram());
    assertEquals(
            "kowshika-test@example.com",
            results.get(0).getEmail()
    );
}


@Test
void findByAgeBetween_returnsDevoteesWithinRange() {

    // Arrange: create three devotees with different ages
    DevotesNames first = new DevotesNames();
    first.setGotram("Kowshika");
    first.setNakshathram("Rohini");
    first.setRaashi("Mithuna");
    first.setAge(20);
    first.setEmail("age20-test@example.com");
    first.setAmount(200.0);

    DevotesNames second = new DevotesNames();
    second.setGotram("Vashishta");
    second.setNakshathram("Ashwini");
    second.setRaashi("Mesha");
    second.setAge(25);
    second.setEmail("age25-test@example.com");
    second.setAmount(250.0);

    DevotesNames third = new DevotesNames();
    third.setGotram("Kashyapa");
    third.setNakshathram("Rohini");
    third.setRaashi("Mithuna");
    third.setAge(40);
    third.setEmail("age40-test@example.com");
    third.setAmount(400.0);

    // Act: persist the test records
    devoteeRepository.saveAndFlush(first);
    devoteeRepository.saveAndFlush(second);
    devoteeRepository.saveAndFlush(third);

    // Find devotees aged between 20 and 30
    List<DevotesNames> results =
            devoteeRepository.findByAgeBetween(20, 30);

    // Assert: ages 20 and 25 should match; 40 should not
    assertEquals(2, results.size());

    assertTrue(results.stream()
            .allMatch(d -> d.getAge() >= 20 && d.getAge() <= 30));

    assertTrue(results.stream()
            .anyMatch(d -> d.getAge() == 20));

    assertTrue(results.stream()
            .anyMatch(d -> d.getAge() == 25));
}

}
