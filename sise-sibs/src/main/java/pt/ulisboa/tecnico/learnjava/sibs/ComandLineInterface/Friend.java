package pt.ulisboa.tecnico.learnjava.sibs.ComandLineInterface;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.ServicesException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.MbwayException;

public class Friend {

	private String phoneNumber;
	private int amount;

	public Friend(String phoneNumber, int amount, Mbway mbway) throws ServicesException, MbwayException {
		if (!this.checkValidMbwayAccount(phoneNumber, amount, mbway)) {
			System.out.println("Oh no, this friend does not have an MbwayAccount");

		} else if (!this.checkEnoughBalance(phoneNumber, amount, mbway)) {
			System.out.println("Oh on, one of your friends does not have money to pay!");
		}

		else {
			this.phoneNumber = phoneNumber;
			this.amount = amount;
		}
	}

	private boolean checkValidMbwayAccount(String phoneNumber, int amount, Mbway mbway) {
		return mbway.getMbwayAccount(phoneNumber) == null ? false : true;

	}

	public boolean checkEnoughBalance(String phoneNumber, int amount, Mbway mbway)
			throws ServicesException, MbwayException {
		return mbway.getMbwayAccount(phoneNumber).isActive()
				&& mbway.getSibs().getServices().canWithdraw(mbway.getIbanByPhoneNumber(phoneNumber), amount);

	}

	public int getAmount() {
		return this.amount;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

}
