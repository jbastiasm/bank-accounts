package com.bastiasj.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String firstName;	
	private String lastName;
	private String iban;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "admin_id", referencedColumnName = "id")
	private Admin admin;

	public User() {
	}

	public User(Admin admin, String firstName, String lastName, String iban) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.iban = iban;
		this.admin = admin;
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

	public Admin getAdmin() {
		return admin;
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

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
}
