package com.bastiasj;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import com.bastiasj.entities.Admin;
import com.bastiasj.services.BankAccountsService;

/**
 * Class to implement the commands
 * 
 * @author bastias
 *
 */
@ShellComponent
public class Commands {

	private static final String STRING_VALIDATION_REGEX = "^[A-Za-z0-9]+";
	private static final String IBAN_VALIDATION_REGEX = "([A-Z]{2})(\\d\\d)(\\d\\d\\d\\d)(\\d\\d\\d\\d)(\\d\\d)(\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d)";
	
	/*************** SERVICE *********************************/

	@Autowired
	private BankAccountsService service;
	
	@Autowired
	private Prompt prompt;

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
	public String login(
			@Size(min = 4, max = 10) 
	        @Pattern(regexp = STRING_VALIDATION_REGEX)
			String login,
			@Size(min = 4, max = 10) 
	        @Pattern(regexp = STRING_VALIDATION_REGEX)
			String password) {
		if (currentAdmin != null) {
			return "Actually you are loggin as '" + currentAdmin.getLogin() + "'.Please, execute logout command.";
		}
		currentAdmin = service.login(login, password);		
		if(currentAdmin != null) { 
			prompt.setActivePrompt(currentAdmin.getLogin());
			return "Admin with login: " + login + ", was logged.";
		} else {
			return "Admin not found or something was wrong with this operation";
		}

	}

	/**
	 * Method to logout admin user or the super admin.
	 * 
	 * @return result message.
	 */
	@ShellMethod("Logout on application.")
	public String logout() {
		prompt.setActivePrompt(null);
		this.currentAdmin = null;
		return "Admin logout";
	}

	// ************** ADMIN COMMANDS *************************/

	/**
	 * Method to create a users administrator.
	 * 
	 * @param login: Admin login
	 * @param password: Admin password
	 * @return result message
	 */
	@ShellMethodAvailability("isSuperAdminCommand")
	@ShellMethod("Add new admin user.")
	public String createadmin(
			@Size(min = 4, max = 10)
	        @Pattern(regexp = STRING_VALIDATION_REGEX)
			String login, 
			@Size(min = 4, max = 10) 
	        @Pattern(regexp = STRING_VALIDATION_REGEX)
			String password) {
		return service.addAdmin(login, password);
	}

	// ************** RESTRICTIONS COMMANDS *************************/
	
	/**
	 * Method to list existing restrictions by users and admin.
	 * This list show restrictions regarding delete, update and list operations on an existing user. 
	 * 
	 * @return Message with all restrictions
	 */
	@ShellMethodAvailability("isSuperAdminCommand")
	@ShellMethod("List admin users.")
	public String listrestrictions() {
		return service.listRestrictions();
	}

	/**
	 * Method to update a particular restriction
	 * 
	 * @param id: id resulting from "listrestictions" command.
	 * @param delete: true - enable, false - disable
	 * @param list: true - enable, false - disable
	 * @param update: true - enable, false - disable
	 * @return result message
	 */
	@ShellMethodAvailability("isSuperAdminCommand")
	@ShellMethod("Update a user on application.")
	public String updaterestriction(Long id, boolean delete, boolean list, boolean update) {
		return service.updateRestriction(id, !delete, !list, !update);
	}

	// ************** USER COMMANDS *************************/

	/**
	 * Method to create a new user.
	 * 
	 * @param firstName: User first name.
	 * @param lastName: User last name.
	 * @param iban: IBAN from SPAIN country as:  ES9121000418450200051332
	 * @return result message
	 */
	@ShellMethodAvailability("isAdminCommand")
	@ShellMethod("Add a new user on application.")
	public String createuser(
			@Size(min = 4, max = 10)
			@Pattern(regexp = STRING_VALIDATION_REGEX)
			String firstName, 
			@Size(min = 4, max = 10) 
	        @Pattern(regexp = STRING_VALIDATION_REGEX)
			String lastName,
	        @Pattern(regexp = IBAN_VALIDATION_REGEX)
			String iban) {
		return service.addUser(currentAdmin, firstName, lastName, iban);
	}

	/**
	 * Method to update an existing user.
	 * 
	 * @param id: User id from "listusers" command.
	 * @param firstName: User first name
	 * @param lastName: User last name
	 * @param iban: IBAN from SPAIN country as:  ES9121000418450200051332 
	 * @return result message
	 */
	@ShellMethodAvailability("isAdminCommand")
	@ShellMethod("Update a user on application.")
	public String updateuser(
			Long id, 
			@Size(min = 4, max = 10) 
            @Pattern(regexp = STRING_VALIDATION_REGEX)
            String firstName,
			@Size(min = 4, max = 10)
	        @Pattern(regexp = STRING_VALIDATION_REGEX)
			String lastName, 
	        @Pattern(regexp = IBAN_VALIDATION_REGEX)
			String iban) {
		service.updateUserById(currentAdmin, id, firstName, lastName, iban);
		return "User was updated";
	}

	/**
	 * Method to delete an existing user.
	 * 
	 * @param id: User id from "listusers" command.
	 * @return result message
	 */
	@ShellMethodAvailability("isAdminCommand")
	@ShellMethod("Delete a user on application.")
	public String deleteuser(Long id) {
		service.deleteUser(currentAdmin, id);
		return "User was deleted";
	}

	/**
	 * Method to listing all user from data base.
	 * 
	 * @return result message
	 */
	@ShellMethodAvailability("isAdminCommand")
	@ShellMethod("List users and accounts")
	public String listusers() {
		return service.listUsers(currentAdmin);
	}

	/**
	 * Method to enable or disable availability for the super-admin user.
	 * 
	 * @return super-admin availability
	 */
	public Availability isSuperAdminCommand() {
		return currentAdmin != null && BankAccountsService.SUPER_ADMIN_LOGIN.equals(currentAdmin.getLogin())
				? Availability.available()
				: Availability.unavailable("Command allowed only for super admin user.");
	}

	/**
	 * 
	 * Method to enable or disable availability for admin users.
 	 * 
	 * @return admin availability
	 */
	public Availability isAdminCommand() {
		return currentAdmin != null && !BankAccountsService.SUPER_ADMIN_LOGIN.equals(currentAdmin.getLogin())
				? Availability.available()
				: Availability.unavailable("Command allowed only for admin users.");
	}

}
