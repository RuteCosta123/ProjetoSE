package pt.ulisboa.tecnico.learnjava.sibs.status;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ServicesException;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;

public class Registered extends State {

	public static State instance = null;

	public static State getInstance() {
		if (instance == null) {
			instance = new Registered();
		}
		return instance;
	}

	@Override
	public void process(TransferOperation transferOperation, Sibs sibs) throws AccountException, ServicesException {
		if (sibs.getServices().canWithdraw(transferOperation.getSourceIban(), transferOperation.getValue())) {

			sibs.getServices().withdraw(transferOperation.getSourceIban(), transferOperation.getValue());
			transferOperation.setState(new Withdrawn());
		} else {

			transferOperation.setState(new StateError());
		}

	}

	@Override
	public void cancel(TransferOperation transferOperation, Sibs sibs) {
		transferOperation.setState(new Cancelled());
	}

}
