package com.temple.Archana.service;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.temple.Archana.exception.DevoteeNotFoundException;
import com.temple.Archana.model.DevotesNames;
import com.temple.Archana.repository.DevoteeRepository;

@Service
public class PoojaService {
	
	private final DevoteeRepository devoteeRepository ;
	
	public PoojaService(DevoteeRepository devoteeRepository) {
		this.devoteeRepository=devoteeRepository;
	}
	
	private final List<DevotesNames> names = new ArrayList<>();
	
	 {
	names.add(new DevotesNames("KowshikaGotram","AswiniNakshatram","MeshaRaashi",10,"kowshika@gmail.com",1007.0));
	names.add(new DevotesNames("BaradwajaGotram","RevathiNakshatram","Meena Raashi",11,"baradwajaa@gmail.com",1006.9));
	names.add(new DevotesNames("VishuGotram","RevathiNakshatram","Meena Raashi",12,"vishu@gmail.com",1005.0));
	names.add(new DevotesNames("ShivaGotram","RevathiNakshatram","Meena Raashi",13,"shiva@gmail.com",1004.9));
	names.add(new DevotesNames("KowdinyasaGotram","RevathiNakshatram","Meena Raashi",14,"kowdinyasa@gmail.com",1003.54));
	names.add(new DevotesNames("NoGotram","RevathiNakshatram","Meena Raashi",15,"nogotram@gmail.com",1002.9));
	}

	 //fetch all students
	public List<DevotesNames> getAllDetails(){
		return devoteeRepository.findAll();
	}
	
	//fetch students by gotram
	public List<DevotesNames> getByGotram(String Gotram){
		
		return devoteeRepository.findByGotram(Gotram);
	}
	
	//fetch students by nakshatram
	public List<DevotesNames> getByNakshatram(String nakshathram){
	
	return	devoteeRepository.findByNakshathram(nakshathram);
	}
	
	//fetch devotees on age
		public List<DevotesNames> getByAgeGreaterThan(int age) {
			return devoteeRepository.findByAgeGreaterThan(age);
		}
		
	//fetch devotee by age using @Query 
		public List<DevotesNames> getByAgethroughQueryparamenter(int age){
			return devoteeRepository.findDevoteesOlderThan(age);
		}
	//fetch devotee by gotram and raashi
		public List<DevotesNames> getByGotramandRaashi(String Gotram,String Raashi){
			return devoteeRepository.findByGotramAndRaashi(Gotram, Raashi);
		}
	//fetch devotee by GR with query paramenter
		public List<DevotesNames> getByGNwithQueryParamenter(String Gotram,String Raashi){
			return devoteeRepository.getbyGRwithquery(Gotram, Raashi);
		}
	//fetch devotee by Gotram or Naksathram
		public List<DevotesNames> getByGotramOrNakshathram(String Gothram,String Nakshathram){
			return devoteeRepository.findByGotramOrNakshathram(Gothram, Nakshathram);
		}
	//fetch devotee by Gotram or Naksathram through query paramenter	
		public List<DevotesNames> getbyGoNbyqueryparameter(String Gotram,String Nakshathram){
			return devoteeRepository.getByGotramorNakshathrambyQuery(Gotram, Nakshathram);
		}
	//fetch devotee by between age 	
		public List<DevotesNames> getByAgeBeween(int from_age,int to_age){
			return devoteeRepository.findByAgeBetween(from_age, to_age);
		}
	//fetch devotee by between age through query 
		public List<DevotesNames> getByagebetweenQuery(int from_age,int to_age){
			return devoteeRepository.findByAgeBetweenQuery(from_age, to_age);
		}
	//fetch devotee by using containing method 
		public List<DevotesNames> findByGotramContating(String text){
			return devoteeRepository.findByGotramContaining(text);
		}
	//fetch devotee by startingwith
		public List<DevotesNames> findByGotramStartingwith(String text){
			return devoteeRepository.findByGotramStartingWith(text);
		}
	//fetch devotee using Endingwith
		public List<DevotesNames> getdevoteeEndingwith(String text){
			return devoteeRepository.findByGotramEndingWith(text);
		}
	//fetch devotee using containg with ignore case
		public List<DevotesNames> getGotramBycontaingIgnorecase(String text){
			return devoteeRepository.findByGotramContainingIgnoreCase(text);
		}
	//check devotee exsists by email
		public boolean isEmailExists(String text) {
			return devoteeRepository.existsByEmail(text);
		}
	//count devotee by gotram
		public long countByGotram(String gotram) {
			return devoteeRepository.countByGotram(gotram);
		}
	//fetch devotee by email
		public List<DevotesNames> getDevoteeByEmail(String email){
			return devoteeRepository.findByEmail(email);
		}
	//update students 
	public List<DevotesNames> updateDevotesByRaashi(DevotesNames updatedRequest,String raashi){
		for(DevotesNames name:names) {
			if(name.getRaashi().equals(raashi)) {
				name.setGotram(updatedRequest.getGotram());
				name.setNakshathram(updatedRequest.getNakshathram());
				name.setRaashi(updatedRequest.getRaashi());
				return names;
			}
		}
		return null;
	}
	
	//Add Devotee by post mapping
	public DevotesNames addStudent(DevotesNames newDevote) {
		
		return devoteeRepository.save(newDevote);
		
	}
	
	//Create Student 
	public DevotesNames createStudent(DevotesNames createDevotee) {
		
		return devoteeRepository.save(createDevotee);
		
	}
	
	

	
	public String deleteStudent(String gotram) {

	    Iterator<DevotesNames> iterator = names.iterator();

	    while (iterator.hasNext()) {

	        DevotesNames name = iterator.next();

	        if (name.getGotram().equals(gotram)) {
	            iterator.remove();
	            return "Deleted Successfully";
	        }
	    }

	    return "Devotee Not Found";
	}
	
	
}
