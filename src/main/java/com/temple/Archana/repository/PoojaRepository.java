package com.temple.Archana.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.temple.Archana.model.Pooja;

import jakarta.persistence.Id;

public interface PoojaRepository extends JpaRepository<Pooja, Long> {

}
