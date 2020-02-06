package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ServicesException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;
import pt.ulisboa.tecnico.learnjava.sibs.status.Completed;
import pt.ulisboa.tecnico.learnjava.sibs.status.Deposited;
import pt.ulisboa.tecnico.learnjava.sibs.status.Registered;
import pt.ulisboa.tecnico.learnjava.sibs.status.StateError;
import pt.ulisboa.tecnico.learnjava.sibs.status.Withdrawn;

public class Sibs {
	final Operation[] operations;
	private Services services;

	public Sibs(int maxNumberOfOperations, Services services) {
		this.operations = new Operation[maxNumberOfOperations];
		this.services = services;
	}

	public Services getServices() {
		return this.services;
	}

	public void payment(String sourceIban, String targetIban, int amount)
			throws SibsException, AccountException, OperationException, ServicesException {

		this.addOperation(Operation.OPERATION_PAYMENT, null, targetIban, amount);

	}

	public void transfer(String sourceIban, String targetIban, int amount)
			throws SibsException, AccountException, OperationException, ServicesException {

		this.addOperation(Operation.OPERATION_TRANSFER, sourceIban, targetIban, amount);

	}

	public void processOperation() throws AccountException, ServicesException, OperationException, SibsException {
//		for (Operation operation : this.operations) {
		for (int i = 0; i < this.operations.length; i++) {

			if (this.operations[i] instanceof TransferOperation) {

				while (!(((TransferOperation) this.operations[i]).getState() instanceof Completed)
						&& !(((TransferOperation) this.operations[i]).getState() instanceof StateError)) {

					((TransferOperation) this.operations[i]).getState().process((TransferOperation) this.operations[i],
							this);

				}
			}

		}

	}

	public int addOperation(String type, String sourceIban, String targetIban, int value)
			throws OperationException, SibsException {
		int position = -1;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] == null) {
				position = i;
				break;
			}
		}

		if (position == -1) {
			throw new SibsException();
		}

		Operation operation;
		if (type.equals(Operation.OPERATION_TRANSFER)) {
			operation = new TransferOperation(sourceIban, targetIban, value);

		} else {
			operation = new PaymentOperation(targetIban, value);
		}

		this.operations[position] = operation;
		return position;
	}

	public void removeOperation(int position) throws SibsException {
		if (position < 0 || position > this.operations.length) {
			throw new SibsException();
		}
		this.operations[position] = null;
	}

	public Operation getOperation(int position) throws SibsException {
		if (position < 0 || position > this.operations.length) {
			throw new SibsException();
		}
		Operation operation = this.operations[position];
		if (operation == null) {
			throw new SibsException();
		} else {
			return operation;
		}
	}

	public int getNumberOfOperations() {
		int result = 0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null) {
				result++;
			}
		}
		return result;
	}

	public int getTotalValueOfOperations() {
		int result = 0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null) {
				result = result + this.operations[i].getValue();
			}
		}
		return result;
	}

	public int getTotalValueOfOperationsForType(String type) {
		int result = 0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null && this.operations[i].getType().equals(type)) {
				result = result + this.operations[i].getValue();
			}
		}
		return result;
	}

	public void cancelOperation(int position)
			throws OperationException, AccountException, ServicesException, SibsException {
		Operation operation = this.getOperation(position);

		if (operation instanceof TransferOperation) {
			if (((TransferOperation) operation).getState() instanceof Deposited
					|| ((TransferOperation) operation).getState() instanceof Registered
					|| ((TransferOperation) operation).getState() instanceof Withdrawn) {

				((TransferOperation) operation).getState().cancel((TransferOperation) operation, this);

			} else {
				throw new OperationException();
			}
		}
	}
}
