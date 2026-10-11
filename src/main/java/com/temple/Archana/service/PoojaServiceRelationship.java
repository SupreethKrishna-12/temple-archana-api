package com.temple.Archana.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.temple.Archana.exception.DevoteeNotFoundException;
import com.temple.Archana.model.DevotesNames;
import com.temple.Archana.model.Pooja;
import com.temple.Archana.repository.DevoteeRepository;
import com.temple.Archana.repository.PoojaRepository;

@Service
public class PoojaServiceRelationship {
	
	private final PoojaRepository poojaRepository ;
	private final DevoteeRepository devoteeRepository;
	
	public PoojaServiceRelationship(PoojaRepository poojaRepository,DevoteeRepository devoteeRepository) {
		this.devoteeRepository = devoteeRepository;
		this.poojaRepository   = poojaRepository;
	}
	
	public Pooja createPooja(String poojaName,Double amount,Long devoteeId) {
		
		DevotesNames devotee = devoteeRepository.findById(devoteeId).orElseThrow(() ->new DevoteeNotFoundException("Devotee with id "+devoteeId+" not found"));
		
		Pooja pooja= new Pooja(poojaName, amount, devotee);
		pooja.setAmount(amount);
		pooja.setPoojaName(poojaName);
		pooja.setDevotee(devotee);
	
		return poojaRepository.save(pooja);		
	}
	
	public DevotesNames getDevoteewithPoojas(Long id) {
		DevotesNames result = devoteeRepository.findById(id).orElseThrow(() -> new DevoteeNotFoundException("Devotee with id "+id+" not found"));
		System.out.println("Pooja Count = "+result.getPoojas().size());
		return result;
	}
	
	@Transactional
	public void testdirtychecking(Long id) {
	 DevotesNames devotee = devoteeRepository.findById(id).orElseThrow(() -> new DevoteeNotFoundException("Devotee with id "+id+" Not found"));
	  devotee.setAge(40);
	 
	}
	
}
