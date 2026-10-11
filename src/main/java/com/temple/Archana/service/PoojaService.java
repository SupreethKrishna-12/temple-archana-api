package com.temple.Archana.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.temple.Archana.dto.CreateDevoteeRequestDTO;
import com.temple.Archana.dto.DevoteeResponseDTO;
import com.temple.Archana.exception.DevoteeNotFoundException;
import com.temple.Archana.model.DevotesNames;
import com.temple.Archana.repository.DevoteeRepository;

@Service
public class PoojaService {

    private final DevoteeRepository devoteeRepository;

    public PoojaService(DevoteeRepository devoteeRepository) {
        this.devoteeRepository = devoteeRepository;
    }

    // ---------- READ ALL ----------

    public List<DevotesNames> getAllDetails() {
        return devoteeRepository.findAll();
    }

    public Page<DevotesNames> getAllDevotees(Pageable pageable) {
        return devoteeRepository.findAll(pageable);
    }

    public List<DevoteeResponseDTO> getAllDevoteesAsDTO() {
        return devoteeRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // ---------- BASIC SEARCHES ----------

    public List<DevotesNames> getByGotram(String gotram) {
        return devoteeRepository.findByGotram(gotram);
    }

    public List<DevotesNames> getByNakshathram(String nakshathram) {
        return devoteeRepository.findByNakshathram(nakshathram);
    }

    public List<DevotesNames> getDevoteeByEmail(String email) {
        return devoteeRepository.findByEmail(email);
    }

    public DevotesNames getDevoteeById(Long id) {
        return devoteeRepository.findById(id)
                .orElseThrow(() -> new DevoteeNotFoundException(
                        "Devotee with id " + id + " not found"
                ));
    }

    // ---------- AGE QUERIES ----------

    public List<DevotesNames> getByAgeGreaterThan(int age) {
        return devoteeRepository.findByAgeGreaterThan(age);
    }

    public List<DevotesNames> getOlderThanUsingQuery(int age) {
        return devoteeRepository.findOlderThanUsingQuery(age);
    }

    public List<DevotesNames> getByAgeBetween(
            int minAge,
            int maxAge
    ) {
        return devoteeRepository.findByAgeBetween(minAge, maxAge);
    }

    public List<DevotesNames> getByAgeBetweenUsingQuery(
            int minAge,
            int maxAge
    ) {
        return devoteeRepository.findByAgeBetweenUsingQuery(
                minAge, maxAge
        );
    }

    // ---------- GOTRAM / RAASHI QUERIES ----------

    public List<DevotesNames> getByGotramAndRaashi(
            String gotram,
            String raashi
    ) {
        return devoteeRepository.findByGotramAndRaashi(
                gotram, raashi
        );
    }

    public List<DevotesNames> getByGotramAndRaashiUsingQuery(
            String gotram,
            String raashi
    ) {
        return devoteeRepository.findByGotramAndRaashiUsingQuery(
                gotram, raashi
        );
    }

    public List<DevotesNames> getByGotramOrNakshathram(
            String gotram,
            String nakshathram
    ) {
        return devoteeRepository.findByGotramOrNakshathram(
                gotram, nakshathram
        );
    }

    public List<DevotesNames> getByGotramOrNakshathramUsingQuery(
            String gotram,
            String nakshathram
    ) {
        return devoteeRepository.findByGotramOrNakshathramUsingQuery(
                gotram, nakshathram
        );
    }

    // ---------- GOTRAM TEXT SEARCHES ----------

    public List<DevotesNames> findByGotramContaining(String text) {
        return devoteeRepository.findByGotramContaining(text);
    }

    public List<DevotesNames> findByGotramStartingWith(String text) {
        return devoteeRepository.findByGotramStartingWith(text);
    }

    public List<DevotesNames> findByGotramEndingWith(String text) {
        return devoteeRepository.findByGotramEndingWith(text);
    }

    public List<DevotesNames> findByGotramContainingIgnoreCase(
            String text
    ) {
        return devoteeRepository.findByGotramContainingIgnoreCase(text);
    }

    // ---------- EXISTENCE AND COUNT ----------

    public boolean isEmailExists(String email) {
        return devoteeRepository.existsByEmail(email);
    }

    public long countByGotram(String gotram) {
        return devoteeRepository.countByGotram(gotram);
    }

    // ---------- PAGINATION + FILTERING + DTO ----------

    public Page<DevoteeResponseDTO> getDevoteesByAgeBetween(
            int minAge,
            int maxAge,
            Pageable pageable
    ) {
        return devoteeRepository
                .findByAgeBetween(minAge, maxAge, pageable)
                .map(this::toResponseDTO);
    }

    // ---------- CREATE ----------

    public DevoteeResponseDTO createDevotee(
            CreateDevoteeRequestDTO request
    ) {
        DevotesNames devotee = new DevotesNames();

        devotee.setGotram(request.getGotram());
        devotee.setNakshathram(request.getNakshathram());
        devotee.setRaashi(request.getRaashi());
        devotee.setAge(request.getAge());
        devotee.setEmail(request.getEmail());
        devotee.setAmount(request.getAmount());

        DevotesNames saved = devoteeRepository.save(devotee);

        return toResponseDTO(saved);
    }

    // Shared save method for the older entity-based endpoints.
    public DevotesNames saveDevotee(DevotesNames devotee) {
        return devoteeRepository.save(devotee);
    }

    // ---------- UPDATE ----------

    public DevotesNames updateDevoteeById(
            DevotesNames request,
            Long id
    ) {
        DevotesNames existing = getDevoteeById(id);

        existing.setGotram(request.getGotram());
        existing.setNakshathram(request.getNakshathram());
        existing.setRaashi(request.getRaashi());
        existing.setAge(request.getAge());
        existing.setAmount(request.getAmount());
        existing.setEmail(request.getEmail());

        return devoteeRepository.save(existing);
    }

    // Legacy endpoint: updates the first record matching Raashi.
    public DevotesNames updateDevoteeByRaashi(
            DevotesNames request,
            String raashi
    ) {
        DevotesNames existing = devoteeRepository
                .findFirstByRaashi(raashi)
                .orElseThrow(() -> new DevoteeNotFoundException(
                        "Devotee with Raashi " + raashi + " not found"
                ));

        existing.setGotram(request.getGotram());
        existing.setNakshathram(request.getNakshathram());
        existing.setRaashi(request.getRaashi());

        return devoteeRepository.save(existing);
    }

    // ---------- DELETE ----------

    public String deleteDevoteeById(Long id) {
        DevotesNames devotee = getDevoteeById(id);

        devoteeRepository.delete(devotee);

        return "Deleted devotee with id " + id + " successfully";
    }

    // Legacy endpoint: deletes the first matching Gotram record.
    public String deleteDevoteeByGotram(String gotram) {
        List<DevotesNames> matches =
                devoteeRepository.findByGotram(gotram);

        if (matches.isEmpty()) {
            return "Devotee Not Found";
        }

        devoteeRepository.delete(matches.get(0));

        return "Deleted Successfully";
    }

    // ---------- ENTITY TO DTO MAPPING ----------

    private DevoteeResponseDTO toResponseDTO(DevotesNames devotee) {
        return new DevoteeResponseDTO(
                devotee.getId(),
                devotee.getGotram(),
                devotee.getNakshathram(),
                devotee.getRaashi(),
                devotee.getAge()
        );
    }
}