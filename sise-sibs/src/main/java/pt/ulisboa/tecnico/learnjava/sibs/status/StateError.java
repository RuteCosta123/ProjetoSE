package pt.ulisboa.tecnico.learnjava.sibs.status;

import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;

public class StateError extends State {
	public static State instance = null;

	public static State getInstance() {
		if (instance == null) {
			instance = new StateError();
		}
		return instance;
	}

	@Override
	public void process(TransferOperation transferOperation, Sibs sibs) {

	}
}
