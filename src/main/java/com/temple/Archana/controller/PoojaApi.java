package com.temple.Archana.controller;

import com.temple.Archana.ArchanaApplication;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.temple.Archana.model.DevotesNames;
import com.temple.Archana.service.PoojaService;

import jakarta.validation.Valid;

@RestController
public class PoojaApi {
	
	
	private final PoojaService poojaService;
	
	PoojaApi(PoojaService poojaService){
		this.poojaService=poojaService;
	}
	
	//GetMapping 
	@GetMapping("/allStudents")
	public List<DevotesNames> getAllDetail() {
		return poojaService.getAllDetails();
	}
	
	@GetMapping("/allStudents/{Gotram}")
	public List<DevotesNames> getDetailsByGotram(@Valid @PathVariable String Gotram){	
		return poojaService.getByGotram(Gotram) ;	
	}
		
	
	@GetMapping("/allStudents/nakshatram/{nakshatram}")
	public List<DevotesNames> getStudentswithnakshaream(@PathVariable String nakshatram){	
		return poojaService.getByNakshatram(nakshatram);
	}

    @GetMapping("/allStudents/age/{age}")
    public List<DevotesNames> getByAgeGreaterThan(@PathVariable int age) {
    	return poojaService.getByAgeGreaterThan(age);
    }
    
    @GetMapping("/allStudents/age/query/{age}")
    public List<DevotesNames> getByAgethroughQueryparamenter(@PathVariable int age){
    	return poojaService.getByAgethroughQueryparamenter(age);
    }
    
    @GetMapping("/allStudents/GR/{Gotram}/{Raashi}")
    public List<DevotesNames> getByGotramandraashi(@PathVariable String Gotram, @PathVariable String Raashi){
    	return poojaService.getByGotramandRaashi(Gotram, Raashi);
    }
    
    @GetMapping("/allStudents/GRquery/{Gotram}/{Raashi}")
    public List<DevotesNames> getByGRwithQuery(@PathVariable String Gotram,@PathVariable String Raashi){
    	return poojaService.getByGNwithQueryParamenter(Gotram, Raashi);
    }
    
    @GetMapping("/allStudents/GON/{Gotram},{Nakshatram}")
    public List<DevotesNames> getByGothramOrNakshathram(@PathVariable String Gotram,@PathVariable String Nakshatram){
    	return poojaService.getByGotramOrNakshathram(Gotram, Nakshatram);
    }
    @GetMapping("/allStudents/ageBetween/{from_age}/{to_age}")
    public List<DevotesNames> getByagebetween(@PathVariable int from_age,@PathVariable int to_age){
    	return poojaService.getByAgeBeween(from_age, to_age);
    }
    @GetMapping("allStudents/ageBetweenQuery/{from_age}/{to_age}")
    public List<DevotesNames> getByageBetweenQuery(@PathVariable int from_age, @PathVariable int to_age){
    	return poojaService.getByagebetweenQuery(from_age, to_age);
    }
    @GetMapping("allStudents/GotramContaining/{text}")
    public List<DevotesNames> findByGogramContaining(@PathVariable String text){
    	return poojaService.findByGotramContating(text);
    }
    @GetMapping("/allStudents/GotramStartingWith/{text}")
    public List<DevotesNames> findByGotramStartingwith(@PathVariable String text){
    	return poojaService.findByGotramStartingwith(text);
    }
    @GetMapping("/allStudents/GotramEndingWith/{text}")
    public List<DevotesNames> getbyGotramendingwith(@PathVariable String text){
    	return poojaService.getdevoteeEndingwith(text);
    }
    @GetMapping("/allStudents/GotramContaingIgnoreCase/{text}")
    	public List<DevotesNames> getbyGotramcontaningignoreCase(@PathVariable String text){
    		return poojaService.getGotramBycontaingIgnorecase(text);
    }
    @GetMapping("/allStudents/GONQuery/{Gotram}/{Nakshatram}")
    public List<DevotesNames> getByGONQuery(@PathVariable String Gotram,@PathVariable String Nakshatram){
    	return poojaService.getbyGoNbyqueryparameter(Gotram, Nakshatram);
    }
    @GetMapping("/allStudents/EmailExists/{email}")
    public boolean IsEmailExists(@PathVariable String email) {
    	return poojaService.isEmailExists(email);
    }
    @GetMapping("/allStudents/countByGotram/{gotram}")
    public long getdevoteecountBygotram(@PathVariable String gotram) {
    	return poojaService.countByGotram(gotram);
    }
    @GetMapping("/allStudents/email/{email}")
    public List<DevotesNames> getDevoteeByEmail(@PathVariable String email){
    	return poojaService.getDevoteeByEmail(email);
    }
    
	//PostMapping
	@PostMapping("/addStudent")
	public ResponseEntity<DevotesNames> addStudent(@Valid @RequestBody DevotesNames newDevote) {
		
	  DevotesNames	results = poojaService.addStudent(newDevote);
	  return ResponseEntity.status(HttpStatus.CREATED).body(results);
	}
	  
	@PostMapping("/createStudents")
	public ResponseEntity<DevotesNames> createDevotee(@RequestBody DevotesNames createDevotee) {
		DevotesNames results =  poojaService.createStudent(createDevotee);
		return ResponseEntity.status(HttpStatus.CREATED).body(results);
	}
	

	
	//PutMapping
	@PutMapping("/updateStudents/{raashi}")
	public ResponseEntity<List<DevotesNames>> updateDevotes(@RequestBody DevotesNames updatedRequest,@PathVariable String raashi){
		
		List<DevotesNames> results =  poojaService.updateDevotesByRaashi(updatedRequest, raashi);
		if (results==null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(results);
	}
	
	//DeleteMapping
	@DeleteMapping("/deleteStudents/{Gotram}")
	public ResponseEntity<String> deleteStudents( @PathVariable String Gotram) {
		String result = poojaService.deleteStudent(Gotram);
		if (result.equals("Devotee Not Found")) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
}
