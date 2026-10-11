package com.temple.Archana.dto;

public class DevoteeResponseDTO {
	
	private Long id;
	private String gotram;
	private String nakshathram;
	private String raashi;
	private int age;
	
	public DevoteeResponseDTO(Long id,String gotram,String nakshathram,String raashi,int age) {
		this.id=id;
		this.gotram=gotram;
		this.nakshathram=nakshathram;
		this.raashi=raashi;
		this.age=age;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGotram() {
		return gotram;
	}

	public void setGotram(String gotram) {
		this.gotram = gotram;
	}

	public String getNakshathram() {
		return nakshathram;
	}

	public void setNakshathram(String nakshathram) {
		this.nakshathram = nakshathram;
	}

	public String getRaashi() {
		return raashi;
	}

	public void setRaashi(String raashi) {
		this.raashi = raashi;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
}
