package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ServicesException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class TransferMethodTestMockingServices {

	private Sibs sibs;
	private Services mockService;
	private String sourceIban = "CTTCK1";
	private String targetIban = "CGDCK2";
	private String targetIban1 = "CTTCK2";
	private int amount = 100;

	@Before
	public void setUp() throws BankException, AccountException, ClientException {
		this.mockService = mock(Services.class);
		this.sibs = new Sibs(3, this.mockService);
	}

	@Test
	public void success() throws SibsException, AccountException, OperationException, ServicesException {
		this.sibs.transfer(this.sourceIban, this.targetIban, this.amount);
		assertEquals(1, this.sibs.getNumberOfOperations());
		verify(this.mockService, never()).deposit(anyString(), anyInt());
		verify(this.mockService, never()).withdraw(anyString(), anyInt());

	}

//	@Test
//	public void successtransferBetweenDifferentBanks() throws BankException, AccountException, SibsException,
//			OperationException, ClientException, ServicesException {
//		when(this.mockService.canWithdraw(anyString(), anyInt())).thenReturn(true);
//		when(this.mockService.canDeposit(anyString(), anyInt())).thenReturn(true);
//
//		this.sibs.transfer(this.sourceIban, this.targetIban, this.amount);
//
//		verify(this.mockService, times(1)).withdraw(this.sourceIban, this.amount);
//		verify(this.mockService, times(1)).withdraw(this.sourceIban, this.sibs.getOperation(0).commission());
//		verify(this.mockService, times(1)).deposit(this.targetIban, this.amount);
//		assertEquals(1, this.sibs.getNumberOfOperations());
//		assertEquals(100, this.sibs.getTotalValueOfOperations());
//		assertEquals(100, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_TRANSFER));
//		assertEquals(0, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_PAYMENT));
//	}
//
//	@Test
//	public void successtransferSameBanks()
//			throws SibsException, AccountException, OperationException, ServicesException {
//
//		this.sibs.transfer(this.sourceIban, this.targetIban1, this.amount);
//
//		verify(this.mockService, atMost(1)).withdraw(anyString(), anyInt());
//		verify(this.mockService, times(1)).deposit(this.targetIban1, this.amount);
//		assertEquals(1, this.sibs.getNumberOfOperations());
//		assertEquals(100, this.sibs.getTotalValueOfOperations());
//		assertEquals(100, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_TRANSFER));
//		assertEquals(0, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_PAYMENT));
//
//	}
//
//	@Test
//	public void transferAfterDepositFails()
//			throws SibsException, AccountException, OperationException, ServicesException {
//		Account mockAccount = mock(Account.class);
//
//		when(this.mockService.getAccountByIban(this.targetIban)).thenReturn(mockAccount);
//		when(mockAccount.isInactive()).thenReturn(true);
//
//		try {
//			this.sibs.transfer(this.sourceIban, this.targetIban, this.amount);
//			fail();
//		} catch (SibsException e) {
//
//			verify(this.mockService, never()).withdraw(this.sourceIban, this.amount);
//			verify(this.mockService, never()).deposit(this.targetIban, this.amount);
//		}
//
//	}

	@After
	public void tearDown() {
		this.sibs = null;
		this.mockService = null;

	}

}
