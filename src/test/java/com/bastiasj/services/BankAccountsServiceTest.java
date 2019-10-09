package com.bastiasj.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataAccessException;

import com.bastiasj.entities.Admin;
import com.bastiasj.entities.Restriction;
import com.bastiasj.entities.User;
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
	private List<User> users;
	private List<Restriction> restrictions;
	
	@Before
	public void setUpBankAccountsServiceInstance() {
		
		admin =new Admin(SUPER_ADMIN_LOGIN, SUPER_ADMIN_PASSWORD);
		
		users=new ArrayList<>();		
		User user1 =new User(admin, "firstName1", "lastName1", "IBAN");
		User user2 =new User(admin, "firstName2", "lastName2", "IBAN");
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
	public void testAdminNotFound() {
		when(adminRepository.findAdminByLogin("admin")).thenReturn(null);
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
	public void testAddAdminException() {
		when(adminRepository.save(any(Admin.class))).thenThrow(DataAccessException.class);
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

}
