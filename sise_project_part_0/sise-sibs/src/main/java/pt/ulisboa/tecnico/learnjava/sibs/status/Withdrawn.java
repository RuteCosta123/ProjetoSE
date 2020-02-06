package pt.ulisboa.tecnico.learnjava.sibs.status;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ServicesException;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;

public class Withdrawn extends State {

	public static State instance = null;

	public static State getInstance() {
		if (instance == null) {
			instance = new Withdrawn();
		}
		return instance;
	}

	@Override
	public void process(TransferOperation transferOperation, Sibs sibs) throws AccountException, ServicesException {

		if (sibs.getServices().canDeposit(transferOperation.getTargetIban(), transferOperation.getValue())) {

			sibs.getServices().deposit(transferOperation.getTargetIban(), transferOperation.getValue());
			if (!transferOperation.getSourceIban().substring(0, 3)
					.equals(transferOperation.getTargetIban().substring(0, 3))) {

				transferOperation.setState(new Deposited());

			} else {
				transferOperation.setState(new Completed());
			}
		} else {
			this.cancel(transferOperation, sibs);
			transferOperation.setState(new StateError());
		}

	}

	@Override
	public void cancel(TransferOperation transferOperation, Sibs sibs) throws ServicesException, AccountException {
		if (sibs.getServices().canDeposit(transferOperation.getSourceIban(), transferOperation.getValue())) {
			sibs.getServices().deposit(transferOperation.getSourceIban(), transferOperation.getValue());
			transferOperation.setState(new Cancelled());
		} else {
			transferOperation.setState(new StateError());
		}

	}
}
