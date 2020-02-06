package pt.ulisboa.tecnico.learnjava.sibs.status;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class GetInstanceMethodTest {

	private State registered;
	private State withdrawn;
	private State deposited;
	private State completed;
	private State stateError;
	private State cancelled;

	@Before
	public void setUp() {
		this.registered = Registered.getInstance();
		this.withdrawn = Withdrawn.getInstance();
		this.deposited = Deposited.getInstance();
		this.completed = Completed.getInstance();
		this.stateError = StateError.getInstance();
		this.cancelled = Cancelled.getInstance();
	}

	@Test
	public void getInstanceRegistered() {
		State registered1 = Registered.getInstance();
		assertEquals(registered1, this.registered);
	}

	@Test
	public void getInstanceWithdrawn() {
		State withdrawn1 = Withdrawn.getInstance();
		assertEquals(withdrawn1, this.withdrawn);
	}

	@Test
	public void getInstanceDeposited() {
		State deposited1 = Deposited.getInstance();
		assertEquals(deposited1, this.deposited);
	}

	@Test
	public void getInstanceCompleted() {
		State completed1 = Completed.getInstance();
		assertEquals(completed1, this.completed);
	}

	@Test
	public void getInstanceCancelled() {
		State cancelled1 = Cancelled.getInstance();
		assertEquals(cancelled1, this.cancelled);
	}

	@Test
	public void getInstanceStateError() {
		State stateError1 = StateError.getInstance();
		assertEquals(stateError1, this.stateError);
	}

}
