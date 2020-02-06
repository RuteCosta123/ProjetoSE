package pt.ulisboa.tecnico.learnjava.sibs.status;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ServicesException;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;

public class Deposited extends State {

	public static State instance = null;

	public static State getInstance() {
		if (instance == null) {
			instance = new Deposited();
		}
		return instance;
	}

	@Override
	public void process(TransferOperation transferOperation, Sibs sibs) throws AccountException, ServicesException {
		if (sibs.getServices().canWithdraw(transferOperation.getSourceIban(), transferOperation.commission())) {

			sibs.getServices().withdraw(transferOperation.getSourceIban(), transferOperation.commission());

			transferOperation.setState(new Completed());
		} else {
			this.cancel(transferOperation, sibs);
			transferOperation.setState(new StateError());

		}
	}

	@Override
	public void cancel(TransferOperation transferOperation, Sibs sibs) throws AccountException, ServicesException {
		if (sibs.getServices().canWithdraw(transferOperation.getTargetIban(), transferOperation.getValue())) {
			sibs.getServices().withdraw(transferOperation.getTargetIban(), transferOperation.getValue());

			Withdrawn.getInstance().cancel(transferOperation, sibs);

		} else {
			transferOperation.setState(new StateError());

		}

	}
}
