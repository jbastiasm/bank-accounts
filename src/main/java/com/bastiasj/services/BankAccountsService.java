package com.bastiasj.services;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bastiasj.entities.Admin;
import com.bastiasj.entities.Restriction;
import com.bastiasj.entities.Users;
import com.bastiasj.repositories.AdminRepository;
import com.bastiasj.repositories.RestrictionsRepository;
import com.bastiasj.repositories.UsersRepository;

/**
 * Bank Account Service class
 * 
 * @author bastias
 */
@Service
public class BankAccountsService {

	public static final String SPLIT = "  |  ";
	public static final String SKIP_LINE = "\n";
	public static final String SUPER_ADMIN_LOGIN = "admin";
	private static final String SUPER_ADMIN_PASSWORD = "1234";
	private static final Admin SUPER_ADMIN = new Admin(SUPER_ADMIN_LOGIN, SUPER_ADMIN_PASSWORD);

	/*************** ERRORS *********************************/

	public static final String ERROR_ADMIN = "There was an error, the new Admin wasn't created.";
	public static final String ERROR_RESTRICTION = "There was an error, the restricction wasn't updated.";
	public static final String ERROR_LIST_USERS = "There was an error getting restriction list.";
	public static final String ERROR_LIST_RESTRICTION = ERROR_LIST_USERS;
	public static final String ERROR_CREATE_USER = "There was an error, the user wasn't created.";
	public static final String ERROR_UPDATE_USER = "There was an error, the user wasn't updated.";
	public static final String ERROR_DELETE_USER = "There was an error, the user wasn't deleted.";

	/*************** REPOSITORIES *********************************/

	Logger logger = LoggerFactory.getLogger(BankAccountsService.class);

	@Autowired
	private UsersRepository usRepository;

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private RestrictionsRepository resRepository;

	public BankAccountsService() {
		super();
	}

	public BankAccountsService(UsersRepository usRepository, AdminRepository adminRepository,
			RestrictionsRepository resRepository) {
		super();
		this.usRepository = usRepository;
		this.adminRepository = adminRepository;
		this.resRepository = resRepository;
	}

	// ************** Common methods *************************/

	public Admin login(String login, String password) {
		try {
			return (SUPER_ADMIN_LOGIN.equals(login) && SUPER_ADMIN_PASSWORD.equals(SUPER_ADMIN_PASSWORD)) ? SUPER_ADMIN
					: adminRepository.findAdminByLogin(login);
		} catch (Exception e) {
			logger.error("There was an exception with login operation, {}", e);
			return null;
		}
	}

	// ************** Admin *************************/

	public String addAdmin(String login, String password) {
		Admin admin = null;
		try {
			admin = adminRepository.save(new Admin(login, password));
		} catch (Exception e) {
			return logException(ERROR_ADMIN, e);
		}
		return admin != null ? "New admin with login: " + login + " created successful." : ERROR_ADMIN;
	}

	// ************** Restrictions *************************/

	public String listRestrictions() {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("================ RESTRICTIONS =======================").append(SKIP_LINE);
			for (Restriction res : resRepository.findAll()) {
				buf.append(StringUtils.rightPad(res.getId().toString(), 4)).append(SPLIT);
				buf.append(StringUtils.rightPad(res.getAdmin().getLogin(), 10)).append(SPLIT);
				buf.append(StringUtils.rightPad(res.getUser().getFirstName() + " " + res.getUser().getLastName(), 22))
						.append(SPLIT);
				buf.append(StringUtils.rightPad("delete (" + res.isDelete() + ")", 10)).append(SPLIT);
				buf.append(StringUtils.rightPad("list (" + res.isList() + ")", 10)).append(SPLIT);
				buf.append(StringUtils.rightPad("update (" + res.isUpdate() + ")", 10)).append(SPLIT).append(SKIP_LINE);
			}
			buf.append("=====================================================").append(SKIP_LINE);
			return buf.toString();
		} catch (Exception e) {
			return logException(ERROR_LIST_RESTRICTION, e);
		}
	}

	public String updateRestriction(Long id, boolean delete, boolean list, boolean update) {
		try {
			return resRepository.updateRestriction(id, delete, list, update) != 0
					? "Restriction was updated succesful."
					: ERROR_RESTRICTION;
		} catch (Exception e) {
			return logException(ERROR_RESTRICTION, e);
		}
	}

	// ************** User *************************/

	public String addUser(Admin currentAdmin, String firstName, String lastName, String iban) {
		Users user = null;
		try {
			if (usRepository.checkDuplicateIBAN(iban) != null) {
				return "IBAN field was already registered. Please, try another one.";
			}
			user = usRepository.save(new Users(firstName, lastName, iban));
			resRepository.save(new Restriction(user, currentAdmin, true, true, true));
			return user != null ? "User was created succesful." : ERROR_CREATE_USER;
		} catch (Exception e) {
			return logException(ERROR_CREATE_USER, e);
		}
	}

	public String listUsers(Admin admin) {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("==================USERS LIST=====================").append(SKIP_LINE);
			for (Users user : usRepository.findAllUsersByAdmin(admin.getId())) {
				buf.append(StringUtils.rightPad(user.getId().toString(), 4)).append(SPLIT);
				buf.append(StringUtils.rightPad(user.getFirstName() + " " + user.getLastName(), 20)).append(SPLIT);
				buf.append(user.getIban()).append(SKIP_LINE);
			}
			buf.append("=================================================").append(SKIP_LINE);
			return buf.toString();

		} catch (Exception e) {
			return logException(ERROR_LIST_USERS, e);
		}
	}

	public String deleteUser(Admin admin, Long id) {
		try {			
			Restriction res =resRepository.getRestriction(admin.getId(), id);
			if(res==null || !res.isDelete()) {
				return "Command don't allowed for restriction.";
			}
			
			final Integer resResult = resRepository.deleteRestriction(admin.getId(), id);
			final Integer userResult = usRepository.deleteUserById(id);
			
			return (resResult == 0 || userResult == 0)
					? ERROR_DELETE_USER 
					: "User deleted successful.";
		} catch (Exception e) {
			return logException(ERROR_DELETE_USER, e);
		}
	}

	public String updateUserById(Admin admin, Long id, String firstName, String lastName, String iban) {
		try {
			Restriction res =resRepository.getRestriction(admin.getId(), id);
			if(res==null || !res.isUpdate()) {
				return "Command don't allowed for restriction.";
			}
			return usRepository.updateUserById(id, firstName, lastName, iban) != 0 ? "User was updated successful."
					: ERROR_UPDATE_USER;
		} catch (Exception e) {
			return logException(ERROR_UPDATE_USER, e);
		}
	}

	// ************** Private Methods *************************/

	private String logException(String message, Exception e) {
		logger.error(message + "{}", e);
		return message;
	}

}
