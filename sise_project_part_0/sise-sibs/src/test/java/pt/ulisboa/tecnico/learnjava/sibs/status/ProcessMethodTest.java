package pt.ulisboa.tecnico.learnjava.sibs.status;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;

//import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ServicesException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class ProcessMethodTest {

	private TransferOperation transferOperation;
	String sourceIban = "CTTCK1";
	String targetIban = "CTTCK2";
	String targetIban1 = "CGDCK3";
	int value = 100;
	Sibs sibs;
	private Withdrawn withdrawn = new Withdrawn();
	private Deposited deposited = new Deposited();
	private Cancelled cancelled = new Cancelled();
	private Services mockService;

	@Before
	public void setUp() throws OperationException {
		this.transferOperation = new TransferOperation(this.sourceIban, this.targetIban, this.value);
		this.mockService = mock(Services.class);
		this.sibs = new Sibs(3, this.mockService);

	}

	@Test
	public void processRegisteredOk() throws AccountException {
		assertTrue(this.transferOperation.getState() instanceof Registered);
	}

	@Test
	public void processRegisteredToWithdrawn() throws AccountException, ServicesException {
		when(this.mockService.canWithdraw(this.sourceIban, this.value)).thenReturn(true);
		this.transferOperation.getState().process(this.transferOperation, this.sibs);

		verify(this.mockService, times(1)).withdraw(this.sourceIban, this.value);
		assertTrue(this.transferOperation.getState() instanceof Withdrawn);
	}

	@Test
	public void processRegisteredToError() throws AccountException, ServicesException {
		when(this.mockService.canWithdraw(this.sourceIban, this.value)).thenReturn(false);
		this.transferOperation.getState().process(this.transferOperation, this.sibs);

		verify(this.mockService, never()).withdraw(this.sourceIban, this.value);
		assertTrue(this.transferOperation.getState() instanceof StateError);

	}

	@Test
	public void processWithdrawnToCompleted() throws AccountException, ServicesException {
		this.transferOperation.setState(this.withdrawn);
		when(this.mockService.canDeposit(this.targetIban, this.value)).thenReturn(true);

		this.transferOperation.getState().process(this.transferOperation, this.sibs);

		verify(this.mockService, times(1)).deposit(this.targetIban, this.value);
		assertTrue(this.transferOperation.getState() instanceof Completed);

	}

	@Test
	public void processWithdrawnToDeposited() throws OperationException, AccountException, ServicesException {
		TransferOperation transferOperation1 = new TransferOperation(this.sourceIban, this.targetIban1, this.value);
		transferOperation1.setState(this.withdrawn);

		when(this.mockService.canDeposit(this.targetIban1, this.value)).thenReturn(true);

		transferOperation1.getState().process(transferOperation1, this.sibs);

		verify(this.mockService, times(1)).deposit(this.targetIban1, this.value);
		assertTrue(transferOperation1.getState() instanceof Deposited);

	}

	@Test
	public void processWithdrawnToError() throws AccountException, ServicesException {
		this.transferOperation.setState(this.withdrawn);
		when(this.mockService.canDeposit(this.targetIban, this.value)).thenReturn(false);

		this.transferOperation.getState().process(this.transferOperation, this.sibs);

		verify(this.mockService, never()).deposit(this.targetIban, this.value);
		assertTrue(this.transferOperation.getState() instanceof StateError);

	}

	@Test
	public void processDepositedToCompleted() throws OperationException, AccountException, ServicesException {
		TransferOperation transferOperation1 = new TransferOperation(this.sourceIban, this.targetIban1, this.value);
		transferOperation1.setState(this.deposited);

		when(this.mockService.canWithdraw(this.sourceIban, transferOperation1.commission())).thenReturn(true);
		transferOperation1.getState().process(transferOperation1, this.sibs);

		verify(this.mockService, times(1)).withdraw(this.sourceIban, transferOperation1.commission());
		assertTrue(transferOperation1.getState() instanceof Completed);

	}

	@Test
	public void processDepositedToError() throws OperationException, AccountException, ServicesException {
		TransferOperation transferOperation1 = new TransferOperation(this.sourceIban, this.targetIban1, this.value);
		transferOperation1.setState(this.deposited);

		when(this.mockService.canWithdraw(this.sourceIban, transferOperation1.commission())).thenReturn(false);
		transferOperation1.getState().process(transferOperation1, this.sibs);

		verify(this.mockService, never()).withdraw(this.sourceIban, transferOperation1.commission());
		assertTrue(transferOperation1.getState() instanceof StateError);

	}

	@Test
	public void processCanceled() throws AccountException, ServicesException {
		this.transferOperation.setState(this.cancelled);
		this.transferOperation.getState().process(this.transferOperation, this.sibs);
		assertTrue(this.transferOperation.getState() instanceof Cancelled);
	}

	@After
	public void tearDown() {
		this.sibs = null;
		this.transferOperation = null;
		this.mockService = null;

	}
}
