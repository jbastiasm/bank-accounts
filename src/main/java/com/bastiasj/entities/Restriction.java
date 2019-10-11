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

	private boolean delete;	
	private boolean update;	
	private boolean list;
		
	public Restriction() {
	}
	public Restriction(User user, Admin admin, boolean userDeleteEnabled, boolean userListEnabled,
			boolean userUpdateEnabled) {
		this.user = user;
		this.admin = admin;
		this.delete = userDeleteEnabled;
		this.list = userListEnabled;
		this.update = userUpdateEnabled;
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
	public void setId(Long id) {
		this.id = id;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
	public boolean isDelete() {
		return delete;
	}
	public boolean isUpdate() {
		return update;
	}
	public boolean isList() {
		return list;
	}
	public void setDelete(boolean delete) {
		this.delete = delete;
	}
	public void setUpdate(boolean update) {
		this.update = update;
	}
	public void setList(boolean list) {
		this.list = list;
	}
}
