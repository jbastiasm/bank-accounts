package com.bastiasj.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.bastiasj.entities.Admin;
import com.bastiasj.entities.Restriction;
import com.bastiasj.entities.Users;
import com.bastiasj.repositories.AdminRepository;
import com.bastiasj.repositories.RestrictionsRepository;
import com.bastiasj.repositories.UsersRepository;

public class BankAccountsServiceTest {
	
	private static final String SUPER_ADMIN_LOGIN = "admin";
	private static final String SUPER_ADMIN_PASSWORD = "1234";
	
	
	private AdminRepository adminRepository =  mock(AdminRepository.class);	
	private RestrictionsRepository resRepository = mock(RestrictionsRepository.class);
	private UsersRepository usRepository = mock(UsersRepository.class);
	private BankAccountsService bankAccountsService;
	private Admin admin;
	private List<Users> users;
	private List<Restriction> restrictions;
	
	@Before
	public void setUpBankAccountsServiceInstance() {
		
		admin =new Admin(SUPER_ADMIN_LOGIN, SUPER_ADMIN_PASSWORD);
		
		users=new ArrayList<>();		
		Users user1 =new Users("firstName1", "lastName1", "IBAN");
		user1.setId(1l);
		Users user2 =new Users("firstName2", "lastName2", "IBAN");
		user2.setId(2l);
		users.add(user1);
		users.add(user2);
		
		restrictions =new ArrayList<>();
		Restriction res1 = new Restriction(user1, new Admin("admin1", "pass1"), true, false, true);
		res1.setId(1l);
		Restriction res2 = new Restriction(user2, new Admin("admin2", "pass2"), true, false, true);
		res2.setId(2l);
		restrictions.add(res1);
		restrictions.add(res2);	
		
		bankAccountsService = new BankAccountsService(usRepository, adminRepository, resRepository);
	}
	
	
	@Test
	public void testSuperAdminLoginFirstTime() {

		when(adminRepository.save(any(Admin.class))).thenReturn(admin);
		assertNotNull(bankAccountsService.login(SUPER_ADMIN_LOGIN, SUPER_ADMIN_PASSWORD));

	}

	@Test
	public void testSuperAdminLoginRestTime() {
		when(adminRepository.findAdminByLogin(SUPER_ADMIN_LOGIN)).thenReturn(admin);
		assertNotNull(bankAccountsService.login(SUPER_ADMIN_LOGIN, SUPER_ADMIN_PASSWORD));
	}

	@Test
	public void testAdminFound() {
		when(adminRepository.findAdminByLogin("admin")).thenReturn(new Admin());
		assertNotNull(bankAccountsService.login("admin", "pass"));
	}

	
	@Test
	public void testAddAdminSuccess() {

		when(adminRepository.save(any(Admin.class))).thenReturn(new Admin("admin", "pass"));
		String message = bankAccountsService.addAdmin("admin", "pass");
		assertNotNull(message);
		assertEquals("New admin with login: admin created successful.", message);
	}

	@Test
	public void testAddAdminError() {

		when(adminRepository.save(any(Admin.class))).thenReturn(null);
		String message = bankAccountsService.addAdmin("admin", "pass");
		assertNotNull(message);
		assertEquals("There was an error, the new Admin wasn't created.", message);
	}

		
	@Test
	public void listRestrictionsWithResults() {
		when(resRepository.findAll()).thenReturn(restrictions);
		String result =bankAccountsService.listRestrictions();
		
		assertNotNull(result);
		
		StringBuffer expected = new StringBuffer();
		
		expected.append("================ RESTRICTIONS =======================").append(BankAccountsService.SKIP_LINE);
		expected.append("1     |  admin1      |  firstName1 lastName1    |  delete (true)  |  list (false)  |  update (true)  |  ").append(BankAccountsService.SKIP_LINE);
		expected.append("2     |  admin2      |  firstName2 lastName2    |  delete (true)  |  list (false)  |  update (true)  |  ").append(BankAccountsService.SKIP_LINE);
		expected.append("=====================================================").append(BankAccountsService.SKIP_LINE);

		assertEquals(expected.toString(), result);

	}
	
	@Test
	public void listRestrictionsWithoutResults() {
		when(resRepository.findAll()).thenReturn(new ArrayList<Restriction>());
		String result =bankAccountsService.listRestrictions();
		
		assertNotNull(result);
		
		StringBuffer expected = new StringBuffer();
		
		expected.append("================ RESTRICTIONS =======================").append(BankAccountsService.SKIP_LINE);
		expected.append("=====================================================").append(BankAccountsService.SKIP_LINE);
		assertEquals(expected.toString(), result);


	}
	
	@Test
	public void listRestrictionsException() {
		restrictions.get(0).setId(null);
		when(resRepository.findAll()).thenReturn(restrictions);
		
		String result = bankAccountsService.listRestrictions();
		
		assertNotNull(result);
		assertEquals(BankAccountsService.ERROR_LIST_RESTRICTION, result);

	}
	
	@Test
	public void updateRestrictionException() throws Exception {
		when(resRepository.updateRestriction(1l, true, false, false)).thenThrow(Exception.class);
		String message = bankAccountsService.updateRestriction(1l, true, false, false);
		assertNotNull(message);
		assertEquals(BankAccountsService.ERROR_RESTRICTION, message);
	}
	

	@Test
	public void updateRestrictionNullResult() throws Exception {
		when(resRepository.updateRestriction(1l, true, false, false)).thenReturn(null);
		String message = bankAccountsService.updateRestriction(1l, true, false, false);
		assertNotNull(message);
		assertEquals(BankAccountsService.ERROR_RESTRICTION, message);
	}
	

	@Test
	public void updateRestrictionSuccess() throws Exception {
		when(resRepository.updateRestriction(1l, true, false, false)).thenReturn(1);
		String message = bankAccountsService.updateRestriction(1l, true, false, false);
		assertNotNull(message);
		assertEquals("Restriction was updated succesful.", message);
	}
	
	@Test
	public void addUserIBANDuplicated() throws Exception {
		when(usRepository.checkDuplicateIBAN("ABCE123")).thenReturn(new Users());
		String message = bankAccountsService.addUser(new Admin(), "firstName", "lastName", "ABCE123");
		assertNotNull(message);
		assertEquals("IBAN field was already registered. Please, try another one.", message);
	}
	
		
	@Test
	public void addUserNullResult() {
		when(usRepository.save(any(Users.class))).thenReturn(null);
		String message = bankAccountsService.addUser(new Admin(), "firstName", "lastName", "ABCE123");
		assertNotNull(message);
		assertEquals(BankAccountsService.ERROR_CREATE_USER, message);
		
	}
	
	@Test
	public void addUserSuccess() {
		when(usRepository.save(any(Users.class))).thenReturn(new Users());
		String message = bankAccountsService.addUser(new Admin(), "firstName", "lastName", "ABCE123");
		assertNotNull(message);
		assertEquals("User was created succesful.", message);
	}
	
	@Test
	public void listUsersEmptyResult() throws Exception {
		when(usRepository.findAllUsersByAdmin(any(Long.class))).thenReturn(new ArrayList<>());
		String message = bankAccountsService.listUsers(new Admin());
		assertNotNull(message);
		
		StringBuffer buf = new StringBuffer();
		buf.append("==================USERS LIST=====================").append(BankAccountsService.SKIP_LINE);
		buf.append("=================================================").append(BankAccountsService.SKIP_LINE);
		
		assertEquals(buf.toString(), message);
	}
	
	@Test
	public void listUsersException() throws Exception {
		when(usRepository.findAllUsersByAdmin(any(Long.class))).thenThrow(Exception.class);
		String message = bankAccountsService.listUsers(new Admin());
		assertNotNull(message);		
		assertEquals(BankAccountsService.ERROR_LIST_USERS, message);
	}
		
	@Test
	public void listUsersSuccess() throws Exception {
		when(usRepository.findAllUsersByAdmin(any(Long.class))).thenReturn(users);
		String message = bankAccountsService.listUsers(new Admin());
		assertNotNull(message);
		
		StringBuffer buf = new StringBuffer();
		buf.append("==================USERS LIST=====================").append(BankAccountsService.SKIP_LINE);
		buf.append("1     |  firstName1 lastName1  |  IBAN").append(BankAccountsService.SKIP_LINE);
		buf.append("2     |  firstName2 lastName2  |  IBAN").append(BankAccountsService.SKIP_LINE);
		buf.append("=================================================").append(BankAccountsService.SKIP_LINE);
		
		assertEquals(buf.toString(), message);
	}
	
	@Test
	public void deleteUserSuccess() throws Exception {
		when(resRepository.deleteRestriction(any(Long.class),any(Long.class))).thenReturn(1);
		when(usRepository.deleteUserById(any(Long.class))).thenReturn(1);
		String message = bankAccountsService.deleteUser(new Admin(), 1l);
		assertNotNull(message);
		assertEquals("User deleted successful.", message);
	}
	
	@Test
	public void deleteUserException() throws Exception {
		when(resRepository.deleteRestriction(any(Long.class),any(Long.class))).thenThrow(Exception.class);
		String message = bankAccountsService.deleteUser(new Admin(), 1l);
		assertNotNull(message);
		assertEquals(BankAccountsService.ERROR_DELETE_USER, message);
	}
	
	@Test
	public void deleteUserNullResult() throws Exception {
		when(resRepository.deleteRestriction(any(Long.class),any(Long.class))).thenReturn(null);
		String message = bankAccountsService.deleteUser(new Admin(), 1l);
		assertNotNull(message);
		assertEquals(BankAccountsService.ERROR_DELETE_USER, message);
	}
	
	@Test
	public void updateUserSuccess() throws Exception {
		when(usRepository.updateUserById(any(Long.class),any(String.class),any(String.class),any(String.class))).thenReturn(1);
		String message = bankAccountsService.updateUserById(new Admin(), 1l,"firstName", "lastName", "ABCE123");
		assertNotNull(message);
		assertEquals("User was updated successful.", message);
	}
	
	@Test
	public void updateUserException() throws Exception {
		when(usRepository.updateUserById(any(Long.class),any(String.class),any(String.class),any(String.class))).thenThrow(Exception.class);
		String message = bankAccountsService.updateUserById(new Admin(), 1l,"firstName", "lastName", "ABCE123");
		assertNotNull(message);
		assertEquals(BankAccountsService.ERROR_UPDATE_USER, message);
	}
	
	@Test
	public void updateUserNullResult() throws Exception {
		when(usRepository.updateUserById(any(Long.class),any(String.class),any(String.class),any(String.class))).thenReturn(null);
		String message = bankAccountsService.updateUserById(new Admin(), 1l,"firstName", "lastName", "ABCE123");
		assertNotNull(message);
		assertEquals(BankAccountsService.ERROR_UPDATE_USER, message);
	}
	

}
