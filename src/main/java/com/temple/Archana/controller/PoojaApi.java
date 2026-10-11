package com.temple.Archana.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.temple.Archana.dto.CreateDevoteeRequestDTO;
import com.temple.Archana.dto.DevoteeResponseDTO;
import com.temple.Archana.model.DevotesNames;
import com.temple.Archana.service.PoojaService;

import jakarta.validation.Valid;

@RestController
public class PoojaApi {

    private final PoojaService poojaService;

    public PoojaApi(PoojaService poojaService) {
        this.poojaService = poojaService;
    }

    // ---------- READ ALL ----------

    @GetMapping("/allStudents")
    public List<DevotesNames> getAllDetails() {
        return poojaService.getAllDetails();
    }

    @GetMapping("/devotees")
    public Page<DevotesNames> getDevotees(Pageable pageable) {
        return poojaService.getAllDevotees(pageable);
    }

    @GetMapping("/devotees/dto")
    public List<DevoteeResponseDTO> getAllDevoteesAsDTO() {
        return poojaService.getAllDevoteesAsDTO();
    }

    // ---------- BASIC SEARCHES ----------

    @GetMapping("/allStudents/{gotram}")
    public List<DevotesNames> getByGotram(
            @PathVariable("gotram") String gotram) {

        return poojaService.getByGotram(gotram);
    }

    @GetMapping("/allStudents/nakshatram/{nakshathram}")
    public List<DevotesNames> getByNakshathram(
            @PathVariable("nakshathram") String nakshathram) {

        return poojaService.getByNakshathram(nakshathram);
    }

    @GetMapping("/allStudents/email-search/{email}")
    public List<DevotesNames> getByEmail(
            @PathVariable("email") String email) {

        return poojaService.getDevoteeByEmail(email);
    }

    @GetMapping("/allStudents/id/{id}")
    public DevotesNames getById(@PathVariable("id") Long id) {
        return poojaService.getDevoteeById(id);
    }

    // ---------- AGE QUERIES ----------

    @GetMapping("/allStudents/age/{age}")
    public List<DevotesNames> getByAgeGreaterThan(
            @PathVariable("age") int age) {

        return poojaService.getByAgeGreaterThan(age);
    }

    @GetMapping("/allStudents/age/query/{age}")
    public List<DevotesNames> getOlderThanUsingQuery(
            @PathVariable("age") int age) {

        return poojaService.getOlderThanUsingQuery(age);
    }

    @GetMapping("/allStudents/ageBetween/{from_age}/{to_age}")
    public List<DevotesNames> getByAgeBetween(
            @PathVariable("from_age") int fromAge,
            @PathVariable("to_age") int toAge) {

        return poojaService.getByAgeBetween(fromAge, toAge);
    }

    @GetMapping("/allStudents/ageBetweenQuery/{from_age}/{to_age}")
    public List<DevotesNames> getByAgeBetweenUsingQuery(
            @PathVariable("from_age") int fromAge,
            @PathVariable("to_age") int toAge) {

        return poojaService.getByAgeBetweenUsingQuery(fromAge, toAge);
    }

    // ---------- GOTRAM / RAASHI QUERIES ----------

    @GetMapping("/allStudents/GR/{Gotram}/{Raashi}")
    public List<DevotesNames> getByGotramAndRaashi(
            @PathVariable("Gotram") String gotram,
            @PathVariable("Raashi") String raashi) {

        return poojaService.getByGotramAndRaashi(gotram, raashi);
    }

    @GetMapping("/allStudents/GRquery/{Gotram}/{Raashi}")
    public List<DevotesNames> getByGotramAndRaashiUsingQuery(
            @PathVariable("Gotram") String gotram,
            @PathVariable("Raashi") String raashi) {

        return poojaService.getByGotramAndRaashiUsingQuery(
                gotram, raashi
        );
    }

    @GetMapping("/allStudents/GON/{Gotram},{Nakshatram}")
    public List<DevotesNames> getByGotramOrNakshathram(
            @PathVariable("Gotram") String gotram,
            @PathVariable("Nakshatram") String nakshathram) {

        return poojaService.getByGotramOrNakshathram(
                gotram, nakshathram
        );
    }

    @GetMapping("/allStudents/GONQuery/{Gotram}/{Nakshatram}")
    public List<DevotesNames> getByGotramOrNakshathramUsingQuery(
            @PathVariable("Gotram") String gotram,
            @PathVariable("Nakshatram") String nakshathram) {

        return poojaService.getByGotramOrNakshathramUsingQuery(
                gotram, nakshathram
        );
    }

    // ---------- GOTRAM TEXT SEARCHES ----------

    @GetMapping("/allStudents/GotramContaining/{text}")
    public List<DevotesNames> getByGotramContaining(
            @PathVariable("text") String text) {

        return poojaService.findByGotramContaining(text);
    }

    @GetMapping("/allStudents/GotramStartingWith/{text}")
    public List<DevotesNames> getByGotramStartingWith(
            @PathVariable("text") String text) {

        return poojaService.findByGotramStartingWith(text);
    }

    @GetMapping("/allStudents/GotramEndingWith/{text}")
    public List<DevotesNames> getByGotramEndingWith(
            @PathVariable("text") String text) {

        return poojaService.findByGotramEndingWith(text);
    }

    @GetMapping("/allStudents/GotramContaingIgnoreCase/{text}")
    public List<DevotesNames> getByGotramContainingIgnoreCase(
            @PathVariable("text") String text) {

        return poojaService.findByGotramContainingIgnoreCase(text);
    }

    // ---------- EXISTENCE AND COUNT ----------

    @GetMapping("/allStudents/EmailExists/{email}")
    public boolean emailExists(@PathVariable("email") String email) {
        return poojaService.isEmailExists(email);
    }

    @GetMapping("/allStudents/countByGotram/{gotram}")
    public long countByGotram(
            @PathVariable("gotram") String gotram) {

        return poojaService.countByGotram(gotram);
    }

    // ---------- PAGINATION + FILTERING + DTO ----------

    @GetMapping("/devotees/by-age")
    public Page<DevoteeResponseDTO> getDevoteesByAgeBetween(
            @RequestParam("minage") int minAge,
            @RequestParam("maxage") int maxAge,
            Pageable pageable) {

        return poojaService.getDevoteesByAgeBetween(
                minAge, maxAge, pageable
        );
    }

    // ---------- CREATE ----------

    @PostMapping("/devotees/create")
    public ResponseEntity<DevoteeResponseDTO> createDevotee(
            @Valid @RequestBody CreateDevoteeRequestDTO request) {

        DevoteeResponseDTO response =
                poojaService.createDevotee(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    // Older entity-based endpoints retained for compatibility.
    @PostMapping("/addStudent")
    public ResponseEntity<DevotesNames> addStudent(
            @Valid @RequestBody DevotesNames request) {

        DevotesNames saved = poojaService.saveDevotee(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(saved);
    }

    @PostMapping("/createStudents")
    public ResponseEntity<DevotesNames> createStudent(
            @Valid @RequestBody DevotesNames request) {

        DevotesNames saved = poojaService.saveDevotee(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(saved);
    }

    // ---------- UPDATE ----------

    @PutMapping("/updateStudents/id/{id}")
    public ResponseEntity<DevotesNames> updateById(
            @Valid @RequestBody DevotesNames request,
            @PathVariable("id") Long id) {

        DevotesNames updated =
                poojaService.updateDevoteeById(request, id);

        return ResponseEntity.ok(updated);
    }

    @PutMapping("/updateStudents/{raashi}")
    public ResponseEntity<List<DevotesNames>> updateByRaashi(
            @RequestBody DevotesNames request,
            @PathVariable("raashi") String raashi) {

        poojaService.updateDevoteeByRaashi(request, raashi);

        // Preserve the existing endpoint's list response.
        return ResponseEntity.ok(poojaService.getAllDetails());
    }

    // ---------- DELETE ----------

    @DeleteMapping("/deleteStudent/id/{id}")
    public ResponseEntity<String> deleteById(
            @PathVariable("id") Long id) {

        String message = poojaService.deleteDevoteeById(id);

        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/deleteStudents/{Gotram}")
    public ResponseEntity<String> deleteByGotram(
            @PathVariable("Gotram") String gotram) {

        String message =
                poojaService.deleteDevoteeByGotram(gotram);

        if ("Devotee Not Found".equals(message)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(message);
        }

        return ResponseEntity.ok(message);
    }
}