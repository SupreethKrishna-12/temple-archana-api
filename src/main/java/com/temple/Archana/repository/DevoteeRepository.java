package com.temple.Archana.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.temple.Archana.model.DevotesNames;

public interface DevoteeRepository
        extends JpaRepository<DevotesNames, Long> {

    // Basic searches
    List<DevotesNames> findByGotram(String gotram);

    List<DevotesNames> findByNakshathram(String nakshathram);

    List<DevotesNames> findByEmail(String email);

    Optional<DevotesNames> findFirstByRaashi(String raashi);

    // Age queries
    List<DevotesNames> findByAgeGreaterThan(int age);

    List<DevotesNames> findByAgeBetween(int minAge, int maxAge);

    Page<DevotesNames> findByAgeBetween(
            int minAge,
            int maxAge,
            Pageable pageable
    );

    @Query("SELECT d FROM DevotesNames d WHERE d.age > :age")
    List<DevotesNames> findOlderThanUsingQuery(
            @Param("age") int age
    );

    @Query("""
           SELECT d FROM DevotesNames d
           WHERE d.age BETWEEN :minAge AND :maxAge
           """)
    List<DevotesNames> findByAgeBetweenUsingQuery(
            @Param("minAge") int minAge,
            @Param("maxAge") int maxAge
    );

    // Gotram and Raashi queries
    List<DevotesNames> findByGotramAndRaashi(
            String gotram,
            String raashi
    );

    @Query("""
           SELECT d FROM DevotesNames d
           WHERE d.gotram = :gotram AND d.raashi = :raashi
           """)
    List<DevotesNames> findByGotramAndRaashiUsingQuery(
            @Param("gotram") String gotram,
            @Param("raashi") String raashi
    );

    // Gotram or Nakshathram queries
    List<DevotesNames> findByGotramOrNakshathram(
            String gotram,
            String nakshathram
    );

    @Query("""
           SELECT d FROM DevotesNames d
           WHERE d.gotram = :gotram OR d.nakshathram = :nakshathram
           """)
    List<DevotesNames> findByGotramOrNakshathramUsingQuery(
            @Param("gotram") String gotram,
            @Param("nakshathram") String nakshathram
    );

    // Text searches
    List<DevotesNames> findByGotramContaining(String text);

    List<DevotesNames> findByGotramStartingWith(String text);

    List<DevotesNames> findByGotramEndingWith(String text);

    List<DevotesNames> findByGotramContainingIgnoreCase(String text);

    // Existence and count
    boolean existsByEmail(String email);

    long countByGotram(String gotram);
}