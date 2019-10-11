package com.bastiasj.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String firstName;	
	private String lastName;
	private String iban;
	
	public User() {
	}

	public User(String firstName, String lastName, String iban) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.iban = iban;
		
	}

	public long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getIban() {
		return iban;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

}
