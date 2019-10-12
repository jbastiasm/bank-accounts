package com.bastiasj;

import org.jline.utils.AttributedString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import com.bastiasj.entities.Admin;
import com.bastiasj.services.BankAccountsService;

@ShellComponent
public class Commands {

	/*************** SERVICE *********************************/

	@Autowired
	private BankAccountsService service;
	
	@Autowired
	private PromptProvider myPromptProvider;

	/*************** CURRENT ADMIN ***********************************/

	private Admin currentAdmin;

	// ************** COMMON COMMANDS *************************/

	/**
	 * Method to login some admin or the super admin in the application.
	 * 
	 * @param login: the admin or super admin login
	 * @param password: the password:
	 * @return result message.
	 */
	@ShellMethod("Login on application.")
	public String login(String login, String password) {
		if (currentAdmin != null) {
			return "Actually you are loggin as '" + currentAdmin.getLogin() + "'.Please, execute logout command.";
		}
		currentAdmin = service.login(login, password);
		myPromptProvider.getPrompt().join(new AttributedString(":"), new AttributedString(login));
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

	// ************** ADMIN COMMANDS *************************/

	@ShellMethodAvailability("isSuperAdminCommand")
	@ShellMethod("Add new admin user.")
	public String createadmin(String login, String password) {
		return service.addAdmin(login, password);
	}

	// ************** RESTRICTIONS COMMANDS *************************/
	@ShellMethodAvailability("isSuperAdminCommand")
	@ShellMethod("List admin users.")
	public String listrestrictions() {
		return service.listRestrictions();
	}

	@ShellMethodAvailability("isSuperAdminCommand")
	@ShellMethod("Update a user on application.")
	public String updaterestriction(Long id, boolean delete, boolean list, boolean update) {
		return service.updateRestriction(id, delete, list, update);
	}

	// ************** USER COMMANDS *************************/

	@ShellMethodAvailability("isAdminCommand")
	@ShellMethod("Add a new user on application.")
	public String createuser(String firstName, String lastName, String iban) {
		return service.addUser(currentAdmin, firstName, lastName, iban);
	}

	@ShellMethodAvailability("isAdminCommand")
	@ShellMethod("Update a user on application.")
	public String updateuser(Long id, String firstName, String lastName, String iban) {
		service.updateUserById(currentAdmin, id, firstName, lastName, iban);
		return "User was updated";
	}

	@ShellMethodAvailability("isAdminCommand")
	@ShellMethod("Delete a user on application.")
	public String deleteuser(Long id) {
		service.deleteUser(currentAdmin, id);
		return "User was deleted";
	}

	@ShellMethodAvailability("isAdminCommand")
	@ShellMethod("List users and accounts")
	public String listusers() {
		return service.listUsers(currentAdmin);
	}

	public Availability isSuperAdminCommand() {
		return currentAdmin != null && BankAccountsService.SUPER_ADMIN_LOGIN.equals(currentAdmin.getLogin())
				? Availability.available()
				: Availability.unavailable("Command allowed only for super admin user.");
	}

	public Availability isAdminCommand() {
		return currentAdmin != null && !BankAccountsService.SUPER_ADMIN_LOGIN.equals(currentAdmin.getLogin())
				? Availability.available()
				: Availability.unavailable("Command allowed only for admin users.");
	}

}
