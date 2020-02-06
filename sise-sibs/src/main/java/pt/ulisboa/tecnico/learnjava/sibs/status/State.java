package pt.ulisboa.tecnico.learnjava.sibs.status;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ServicesException;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;

public abstract class State {

	public void process(TransferOperation transferOperation, Sibs sibs) throws AccountException, ServicesException {

	}

	public void cancel(TransferOperation transferOperation, Sibs sibs) throws AccountException, ServicesException {

	}

}
