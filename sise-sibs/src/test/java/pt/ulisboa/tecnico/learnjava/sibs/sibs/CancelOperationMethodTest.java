package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ServicesException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;
import pt.ulisboa.tecnico.learnjava.sibs.status.Cancelled;
import pt.ulisboa.tecnico.learnjava.sibs.status.Completed;
import pt.ulisboa.tecnico.learnjava.sibs.status.Deposited;
import pt.ulisboa.tecnico.learnjava.sibs.status.State;
import pt.ulisboa.tecnico.learnjava.sibs.status.StateError;
import pt.ulisboa.tecnico.learnjava.sibs.status.Withdrawn;

public class CancelOperationMethodTest {

	private String sourceIban = "CTTCK1";
	private String targetIban = "CTTCK2";
	private String targetIban1 = "CGDCK3";
	private int value = 100;
	private int firstOperation;
	private int secondOperation;
	private Sibs sibs;
	private Withdrawn withdrawn = new Withdrawn();
	private Deposited deposited = new Deposited();
	private Completed completed = new Completed();
	private StateError stateError = new StateError();
	private State cancelled = new Cancelled();
	private Services mockService;

	@Before
	public void setUp() throws OperationException, SibsException {
		this.mockService = mock(Services.class);
		this.sibs = new Sibs(3, this.mockService);
		this.firstOperation = this.sibs.addOperation(Operation.OPERATION_TRANSFER, this.sourceIban, this.targetIban,
				this.value);
		this.secondOperation = this.sibs.addOperation(Operation.OPERATION_TRANSFER, this.sourceIban, this.targetIban1,
				this.value);
	}

	@Test
	public void successCancelRegistered()
			throws OperationException, AccountException, ServicesException, SibsException {
		TransferOperation transferOperation = (TransferOperation) (this.sibs.getOperation(this.firstOperation));
		this.sibs.cancelOperation(0);
		assertTrue(transferOperation.getState() instanceof Cancelled);
		verify(this.mockService, never()).deposit(anyString(), anyInt());
		verify(this.mockService, never()).withdraw(anyString(), anyInt());

	}

	@Test
	public void successCancelWithdrawn() throws OperationException, AccountException, ServicesException, SibsException {
		TransferOperation transferOperation = (TransferOperation) (this.sibs.getOperation(this.firstOperation));
		transferOperation.setState(this.withdrawn);
		when(this.mockService.canDeposit(this.sourceIban, this.value)).thenReturn(true);
		this.sibs.cancelOperation(0);

		assertTrue(transferOperation.getState() instanceof Cancelled);
		verify(this.mockService, times(1)).deposit(this.sourceIban, this.value);
		verify(this.mockService, never()).withdraw(anyString(), anyInt());

	}

	@Test
	public void successCancelDeposited() throws OperationException, AccountException, ServicesException, SibsException {
		TransferOperation transferOperation = (TransferOperation) (this.sibs.getOperation(this.secondOperation));
		transferOperation.setState(this.deposited);
		when(this.mockService.canWithdraw(this.targetIban1, this.value)).thenReturn(true);
		when(this.mockService.canDeposit(this.sourceIban, this.value)).thenReturn(true);
		this.sibs.cancelOperation(1);

		assertTrue(transferOperation.getState() instanceof Cancelled);
		verify(this.mockService, times(1)).withdraw(this.targetIban1, this.value);
		verify(this.mockService, times(1)).deposit(this.sourceIban, this.value);

	}

	@Test
	public void CancelCompleted() throws OperationException, AccountException, ServicesException, SibsException {
		TransferOperation transferOperation = (TransferOperation) (this.sibs.getOperation(this.secondOperation));
		transferOperation.setState(this.completed);

		try {
			this.sibs.cancelOperation(1);
			fail();
		} catch (OperationException e) {
			assertTrue(transferOperation.getState() instanceof Completed);
			verify(this.mockService, never()).withdraw(anyString(), anyInt());
			verify(this.mockService, never()).deposit(anyString(), anyInt());
		}
	}

	@Test
	public void CancelStateError() throws OperationException, AccountException, ServicesException, SibsException {
		TransferOperation transferOperation = (TransferOperation) (this.sibs.getOperation(this.secondOperation));
		transferOperation.setState(this.stateError);

		try {
			this.sibs.cancelOperation(1);
			fail();
		} catch (OperationException e) {
			assertTrue(transferOperation.getState() instanceof StateError);
			verify(this.mockService, never()).withdraw(anyString(), anyInt());
			verify(this.mockService, never()).deposit(anyString(), anyInt());
		}
	}

	@Test
	public void CancelCanceled() throws OperationException, AccountException, ServicesException, SibsException {
		TransferOperation transferOperation = (TransferOperation) (this.sibs.getOperation(this.firstOperation));
		transferOperation.setState(this.cancelled);

		try {
			this.sibs.cancelOperation(0);
			fail();
		} catch (OperationException e) {
			assertTrue(transferOperation.getState() instanceof Cancelled);
			verify(this.mockService, never()).withdraw(anyString(), anyInt());
			verify(this.mockService, never()).deposit(anyString(), anyInt());
		}
	}

	@Test
	public void cancelNonExistingOperation()
			throws OperationException, AccountException, ServicesException, SibsException {

		try {
			this.sibs.cancelOperation(4);
			fail();
		} catch (SibsException e) {

		}
	}

	@Test
	public void cancelUnvalidPosition() throws OperationException, AccountException, ServicesException, SibsException {

		try {
			this.sibs.cancelOperation(-3);
			fail();
		} catch (SibsException e) {

		}
	}

	@After
	public void tearDown() {
		this.mockService = null;
		this.sibs = null;

	}
}
