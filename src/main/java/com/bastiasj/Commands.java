package com.bastiasj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import com.bastiasj.entities.Admin;
import com.bastiasj.services.BankAccountsService;

@ShellComponent
public class Commands {
		
	/*************** SERVICE *********************************/	
	
	@Autowired
	private BankAccountsService service;	
	
	/*************** CURRENT ADMIN ***********************************/	
	
	private Admin currentAdmin;	
	
	//************** COMMON COMMANDS *************************/
	
	/**
	 * Method to login some admin or the super admin in the application.
	 * 
	 * @param login: the admin or super admin login
	 * @param password: the password:
	 * @return result message.
	 */
	@ShellMethod("Login on application.")
	public String login(String login, String password) {

		currentAdmin = service.login(login, password);
		return currentAdmin != null ? "Admin with login: " + login + ", was logged."
				: "Admin not found or something was wrong with this operation";

	}

	/**
	 * Method to logout admin user or the super admin.
	 * 
	 * @return result message.
	 */
	@ShellMethod("Logout on application.")
	public String logout() {
		this.currentAdmin = null;
		return "Admin logout";
	}
	
	//************** ADMIN COMMANDS *************************/
	
	@ShellMethod("Add new admin user.")
	public String createadmin(String login, String password) {
		return service.addAdmin(login, password);
	}
	
	//************** RESTRICTIONS COMMANDS *************************/
	
	@ShellMethod("List admin users.")
	public String listrestrictions() {
		return service.listRestrictions();
	}
	
	@ShellMethod("Update a user on application.")
	public String updaterestriction(Long id, boolean delete, boolean list, boolean update) {
		return service.updateRestriction(id, delete, list, update);
	}

	//************** USER COMMANDS *************************/
	
	@ShellMethod("Add a new user on application.")
	public String createuser(String firstName, String lastName, String iban) {
		return service.addUser(currentAdmin, firstName, lastName, iban);
	}

	@ShellMethod("Update a user on application.")
	public String updateuser(Long id, String firstName, String lastName, String iban) {
		service.updateUserById(id, firstName, lastName, iban);
		return "User was updated";
	}

	@ShellMethod("Delete a user on application.")
	public String deleteuser(Long id) {
		service.deleteUser(id);
		return "User was deleted";
	}

	@ShellMethod("List users and accounts")
	public String listusers() {
		return service.listUsers(currentAdmin);
	}
	
}
