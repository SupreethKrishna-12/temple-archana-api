package com.temple.Archana.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.temple.Archana.model.Pooja;
import com.temple.Archana.service.PoojaServiceRelationship;

@RestController
public class PoojaRelationshipController {
		
	private final PoojaServiceRelationship poojaServiceRelationship;
	
		public PoojaRelationshipController(PoojaServiceRelationship poojaServiceRelationship) {
			this.poojaServiceRelationship=poojaServiceRelationship;
		}
		
		@PostMapping("/poojas/devotee/{devoteeId}")
		public ResponseEntity<Pooja> createPooja(@PathVariable Long devoteeId, @RequestBody Pooja pooja) {
			Pooja result = poojaServiceRelationship.createPooja(pooja.getPoojaName(), pooja.getAmount(), devoteeId);	
			return ResponseEntity.status(HttpStatus.CREATED).body(result);
		}
}
