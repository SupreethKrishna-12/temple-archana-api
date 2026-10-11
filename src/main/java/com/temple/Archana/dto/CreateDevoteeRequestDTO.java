package com.temple.Archana.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CreateDevoteeRequestDTO {
		
	@NotBlank(message = "Gotram is required")
	@Size(min = 3, max = 10, message = "Gotram must be 3–10 characters")
	@Pattern(regexp = "^[A-Za-z ]+$",
	         message = "Gotram must contain only letters and spaces")
	private String gotram;

	@NotBlank(message = "Nakshathram is required")
	private String nakshathram;

	@NotBlank(message = "Raashi is required")
	private String raashi;

	@NotNull(message = "Age is required")
	@Min(value = 18, message = "Age must be at least 18")
	@Max(value = 100, message = "Age cannot exceed 100")
	private Integer age;

	@NotBlank(message = "Email is required")
	@Email(message = "Please provide a valid email")
	private String email;

	@NotNull(message = "Amount is required")
	@Positive(message = "Amount must be positive")
	private Double amount;
	
	public CreateDevoteeRequestDTO(String gotram,String nakshathram,String raashi, Integer age, String email, Double amount) {
		this.gotram=gotram;
		this.nakshathram=nakshathram;
		this.raashi=raashi;
		this.age=age;
		this.email=email;
		this.amount=amount;
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

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
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
		this.amount = amount;
	}	
}
