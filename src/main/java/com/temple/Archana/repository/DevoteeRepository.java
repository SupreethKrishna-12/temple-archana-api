package com.temple.Archana.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.temple.Archana.model.DevotesNames;

public interface DevoteeRepository extends JpaRepository<DevotesNames, Long> {

	List<DevotesNames> findByGotram(String gotram);
	List<DevotesNames> findByNakshathram(String nakshathram);
	
	List<DevotesNames> findByAgeGreaterThan(int age);
	
	@Query("SELECT d FROM DevotesNames d WHERE d.age > :age")
	List<DevotesNames> findDevoteesOlderThan(@Param("age") int age);
	
	List<DevotesNames> findByGotramAndRaashi(String gotram,String raashi);
	
	@Query("select d from DevotesNames d where d.gotram = :gotram and d.raashi = :raashi")
	List<DevotesNames> getbyGRwithquery(@Param("gotram") String gotram, @Param("raashi") String raashi);
	
	List<DevotesNames> findByGotramOrNakshathram(String Gotram,String Nakshathram);

	@Query("SELECT d from DevotesNames d where d.gotram = :gotram or d.nakshathram = :nakshathram")
	List<DevotesNames> getByGotramorNakshathrambyQuery(@Param("gotram") String gotram, @Param("nakshathram") String nakshathram);
	
	List<DevotesNames> findByAgeBetween(int from_age, int to_age);
	
	@Query("select d from DevotesNames d where d.age between :from_age and :to_age")
	List<DevotesNames> findByAgeBetweenQuery(@Param("from_age") int from_age, @Param("to_age") int to_age);
	
	List<DevotesNames> findByGotramContaining(String text);
	
	List<DevotesNames> findByGotramStartingWith(String text);
	
	List<DevotesNames> findByGotramEndingWith(String text);
	
	List<DevotesNames> findByGotramContainingIgnoreCase(String text);
	
	boolean existsByEmail(String email);
	
	long countByGotram(String gotram);
	
}
