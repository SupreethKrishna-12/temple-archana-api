package com.temple.Archana.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="poojas")
public class Pooja {
		
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		
		private String poojaName;
		
		private Double amount;
		
		@ManyToOne
		@JoinColumn(name = "devotee_id")
		private DevotesNames devotee;
		
		public Pooja(String poojaName,Double amount,DevotesNames devotee) {
			this.poojaName=poojaName;
			this.amount=amount;
			this.devotee=devotee;
		}
		
		
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getPoojaName() {
			return poojaName;
		}
		public void setPoojaName(String poojaName) {
			this.poojaName = poojaName;
		}
		public Double getAmount() {
			return amount;
		}
		public void setAmount(Double amount) {
			this.amount = amount;
		}
		public DevotesNames getDevotee() {
			return devotee;
		}
		public void setDevotee(DevotesNames devotee) {
			this.devotee = devotee;
		}
		
	
}
