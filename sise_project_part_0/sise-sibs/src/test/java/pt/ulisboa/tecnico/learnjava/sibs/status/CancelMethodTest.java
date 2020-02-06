package pt.ulisboa.tecnico.learnjava.sibs.status;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
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

public class CancelMethodTest {

	private TransferOperation transferOperation;
	private String sourceIban = "CTTCK1";
	private String targetIban = "CTTCK2";
	private int value = 100;
	private Sibs sibs;
	private Withdrawn withdrawn = new Withdrawn();
	private Deposited deposited = new Deposited();
	private Completed completed = new Completed();
	private StateError stateError = new StateError();
	private Services mockService;

	@Before
	public void setUp() throws OperationException {
		this.transferOperation = new TransferOperation(this.sourceIban, this.targetIban, this.value);
		this.mockService = mock(Services.class);
		this.sibs = new Sibs(3, this.mockService);

	}

	@Test
	public void cancelRegisteredOk() throws AccountException, ServicesException {
		this.transferOperation.getState().cancel(this.transferOperation, this.sibs);
		assertTrue(this.transferOperation.getState() instanceof Cancelled);
	}

	@Test
	public void cancelWithdrawn() throws AccountException, ServicesException {
		this.transferOperation.setState(this.withdrawn);

		when(this.mockService.canDeposit(this.sourceIban, this.value)).thenReturn(true);
		this.transferOperation.getState().cancel(this.transferOperation, this.sibs);

		verify(this.mockService, times(1)).deposit(this.sourceIban, this.value);
		assertTrue(this.transferOperation.getState() instanceof Cancelled);
	}

	@Test
	public void cancelWithdrawnError() throws AccountException, ServicesException {
		this.transferOperation.setState(this.withdrawn);

		when(this.mockService.canDeposit(this.sourceIban, this.value)).thenReturn(false);
		this.transferOperation.getState().cancel(this.transferOperation, this.sibs);

		verify(this.mockService, never()).deposit(this.sourceIban, this.value);
		assertTrue(this.transferOperation.getState() instanceof StateError);
	}

	@Test
	public void cancelDeposited() throws AccountException, ServicesException {
		this.transferOperation.setState(this.deposited);

		when(this.mockService.canWithdraw(this.targetIban, this.value)).thenReturn(true);
		when(this.mockService.canDeposit(this.sourceIban, this.value)).thenReturn(true);

		this.transferOperation.getState().cancel(this.transferOperation, this.sibs);

		verify(this.mockService, times(1)).withdraw(this.targetIban, this.value);
		verify(this.mockService, times(1)).deposit(this.sourceIban, this.value);
		assertTrue(this.transferOperation.getState() instanceof Cancelled);

	}

	@Test
	public void cancelDepositedError() throws AccountException, ServicesException {
		this.transferOperation.setState(this.deposited);

		when(this.mockService.canWithdraw(this.sourceIban, this.value)).thenReturn(false);
		this.transferOperation.getState().cancel(this.transferOperation, this.sibs);

		verify(this.mockService, never()).withdraw(this.targetIban, this.value);
		assertTrue(this.transferOperation.getState() instanceof StateError);

	}

	@Test
	public void cancelCompleted() throws AccountException, ServicesException {
		this.transferOperation.setState(this.completed);

		this.transferOperation.getState().cancel(this.transferOperation, this.sibs);

		verify(this.mockService, never()).deposit(anyString(), anyInt());
		verify(this.mockService, never()).withdraw(anyString(), anyInt());
		assertTrue(this.transferOperation.getState() instanceof Completed);

	}

	@Test
	public void cancelStateError() throws AccountException, ServicesException {
		this.transferOperation.setState(this.stateError);

		this.transferOperation.getState().cancel(this.transferOperation, this.sibs);

		verify(this.mockService, never()).deposit(anyString(), anyInt());
		verify(this.mockService, never()).withdraw(anyString(), anyInt());
		assertTrue(this.transferOperation.getState() instanceof StateError);

	}

	@After
	public void tearDown() {
		this.sibs = null;
		this.transferOperation = null;
		this.mockService = null;

	}
}
