package com.bastiasj.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Restriction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(cascade = { CascadeType.MERGE })
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@ManyToOne(cascade = { CascadeType.MERGE })
	@JoinColumn(name = "admin_id", referencedColumnName = "id")
	private Admin admin;

	private boolean userDeleteEnabled;	
	private boolean userListEnabled;	
	private boolean userUpdateEnabled;
		
	public Restriction() {
	}
	public Restriction(User user, Admin admin, boolean userDeleteEnabled, boolean userListEnabled,
			boolean userUpdateEnabled) {
		this.user = user;
		this.admin = admin;
		this.userDeleteEnabled = userDeleteEnabled;
		this.userListEnabled = userListEnabled;
		this.userUpdateEnabled = userUpdateEnabled;
	}
	public Long getId() {
		return id;
	}
	public User getUser() {
		return user;
	}
	public Admin getAdmin() {
		return admin;
	}
	public boolean isUserDeleteEnabled() {
		return userDeleteEnabled;
	}
	public boolean isUserListEnabled() {
		return userListEnabled;
	}
	public boolean isUserUpdateEnabled() {
		return userUpdateEnabled;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
	public void setUserDeleteEnabled(boolean userDeleteEnabled) {
		this.userDeleteEnabled = userDeleteEnabled;
	}
	public void setUserListEnabled(boolean userListEnabled) {
		this.userListEnabled = userListEnabled;
	}
	public void setUserUpdateEnabled(boolean userUpdateEnabled) {
		this.userUpdateEnabled = userUpdateEnabled;
	}

}
