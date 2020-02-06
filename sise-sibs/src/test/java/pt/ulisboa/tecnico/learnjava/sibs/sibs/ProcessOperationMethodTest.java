package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ServicesException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;
import pt.ulisboa.tecnico.learnjava.sibs.status.Completed;
import pt.ulisboa.tecnico.learnjava.sibs.status.StateError;

public class ProcessOperationMethodTest {

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
	public void successProcessTransferBetweenDifferentBanks() throws BankException, AccountException, SibsException,
			OperationException, ClientException, ServicesException {
		when(this.mockService.canWithdraw(anyString(), anyInt())).thenReturn(true);
		when(this.mockService.canDeposit(anyString(), anyInt())).thenReturn(true);

		this.sibs.transfer(this.sourceIban, this.targetIban, this.amount);
		this.sibs.processOperation();

		verify(this.mockService, times(1)).withdraw(this.sourceIban, this.amount);
		verify(this.mockService, times(1)).withdraw(this.sourceIban, this.sibs.getOperation(0).commission());
		verify(this.mockService, times(1)).deposit(this.targetIban, this.amount);
		assertEquals(1, this.sibs.getNumberOfOperations());
		assertEquals(100, this.sibs.getTotalValueOfOperations());
		assertEquals(100, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_TRANSFER));
		assertEquals(0, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_PAYMENT));
	}

	@Test
	public void successProcessOperationTransferSameBanks()
			throws SibsException, AccountException, OperationException, ServicesException {
		when(this.mockService.canWithdraw(anyString(), anyInt())).thenReturn(true);
		when(this.mockService.canDeposit(anyString(), anyInt())).thenReturn(true);
		this.sibs.transfer(this.sourceIban, this.targetIban1, this.amount);

		this.sibs.processOperation();

		verify(this.mockService, atMost(1)).withdraw(anyString(), anyInt());
		verify(this.mockService, times(1)).deposit(this.targetIban1, this.amount);

		assertTrue((((TransferOperation) this.sibs.getOperation(0)).getState()) instanceof Completed);

	}

	@Test
	public void errorProcessOperationTransferSameBanks()
			throws SibsException, AccountException, OperationException, ServicesException {
		when(this.mockService.canWithdraw(anyString(), anyInt())).thenReturn(true);
		when(this.mockService.canDeposit(this.targetIban, this.amount)).thenReturn(false);
		when(this.mockService.canDeposit(this.sourceIban, this.amount)).thenReturn(true);
		this.sibs.transfer(this.sourceIban, this.targetIban1, this.amount);

		this.sibs.processOperation();

		verify(this.mockService, times(1)).withdraw(anyString(), anyInt());
		verify(this.mockService, times(2)).canDeposit(anyString(), anyInt());
		verify(this.mockService, times(1)).deposit(this.sourceIban, this.amount);

		assertTrue((((TransferOperation) this.sibs.getOperation(0)).getState()) instanceof StateError);

	}

	@Test
	public void successProcessVariousTransferOperations() throws BankException, AccountException, SibsException,
			OperationException, ClientException, ServicesException {
		when(this.mockService.canWithdraw(anyString(), anyInt())).thenReturn(true);
		when(this.mockService.canDeposit(anyString(), anyInt())).thenReturn(true);

		this.sibs.transfer(this.sourceIban, this.targetIban, this.amount);
		this.sibs.transfer(this.sourceIban, this.targetIban, this.amount);
		this.sibs.transfer(this.sourceIban, this.targetIban, this.amount);
		this.sibs.processOperation();

		verify(this.mockService, times(3)).withdraw(this.sourceIban, this.amount);
		verify(this.mockService, times(3)).withdraw(this.sourceIban, this.sibs.getOperation(0).commission());
		verify(this.mockService, times(3)).deposit(this.targetIban, this.amount);
		assertEquals(3, this.sibs.getNumberOfOperations());
		assertTrue(((TransferOperation) this.sibs.getOperation(0)).getState() instanceof Completed);
		assertTrue(((TransferOperation) this.sibs.getOperation(1)).getState() instanceof Completed);
		assertTrue(((TransferOperation) this.sibs.getOperation(2)).getState() instanceof Completed);

	}

	@Test
	public void successProcessNoneTransferOperations() throws BankException, AccountException, SibsException,
			OperationException, ClientException, ServicesException {
		when(this.mockService.canWithdraw(anyString(), anyInt())).thenReturn(true);
		when(this.mockService.canDeposit(anyString(), anyInt())).thenReturn(true);

		this.sibs.processOperation();

		verify(this.mockService, never()).withdraw(anyString(), anyInt());
		verify(this.mockService, never()).deposit(anyString(), anyInt());
		assertEquals(0, this.sibs.getNumberOfOperations());

	}

	@After
	public void tearDown() {
		this.sibs = null;
		this.mockService = null;

	}

}
