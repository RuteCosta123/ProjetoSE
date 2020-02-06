package pt.ulisboa.tecnico.learnjava.sibs.status;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ServicesException;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;

public class Cancelled extends State {

	public static State instance = null;

	public static State getInstance() {
		if (instance == null) {
			instance = new Cancelled();
		}
		return instance;
	}

	@Override
	public void process(TransferOperation transferOperation, Sibs sibs) throws AccountException, ServicesException {
		System.out.println("This operation is not valid for this status");
	}
}
