package com.temple.Archana.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "devotees")
public class DevotesNames {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Gotram must not be NULL")
	@Size(min = 3, max = 10,message = " Gotram name should be in between 3 and 10 characters")
	@Pattern(regexp = "^[A-Za-z ]+$",message = "Gotram name should contain only alphbets and space")
	private String gotram;
	
	@NotBlank(message = "Nakshatram must not be Null")
	private String nakshathram;
	
	@NotBlank(message = "Raashi must not be NULL")
	private String raashi;
	
	@Min(value = 18,message = "Age should be greater than or equal to 18")
	@Max(value = 100,message = "Age should not greater than 100")
	private int age;
	
	@NotBlank(message = "Please provide email id")
	@Email(message = "Please provide valid email id ")
	private String email;
	
	@NotNull(message = "Amount is mandatory")
	@Positive(message = "Amount shoud be positive")
	private Double amount;
	
	public DevotesNames(String gotram,String nakshathram, String raashi,int age,String email,Double amount) {
		this.gotram=gotram;
		this.nakshathram=nakshathram;
		this.raashi=raashi;
		this.age=age;
		this.email=email;
		this.amount=amount;
	}
	public DevotesNames() {
		
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id=id;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount=amount;
	}
	
}
