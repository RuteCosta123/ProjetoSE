package pt.ulisboa.tecnico.learnjava.sibs.ComandLineInterface;

import java.util.HashMap;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.MbwayException;

public class Mbway {

	private Sibs sibs;
	private Services service = new Services();
	private HashMap<String, MbwayAccount> mbwayAccounts = new HashMap<>();
	public static Mbway instance = null;

	public Mbway() {
		this.sibs = new Sibs(100, this.service);
	}

	public static Mbway getInstance() {
		if (instance == null) {
			instance = new Mbway();
		}
		return instance;
	}

	public MbwayAccount getMbwayAccount(String phoneNumber) {
		return this.mbwayAccounts.get(phoneNumber);
	}

	public void addMbwayAccount(MbwayAccount mbwayAccount) {
		this.mbwayAccounts.put(mbwayAccount.getPhoneNumber(), mbwayAccount);
	}

	public boolean checkExistingIban(String iban) {
		for (MbwayAccount mbwayAccount : this.mbwayAccounts.values()) {
			if (mbwayAccount.getIban().equals(iban)) {
				return true;
			}
		}
		return false;
	}

	public String getIbanByPhoneNumber(String phoneNumber) throws MbwayException {
		return this.mbwayAccounts.get(phoneNumber).getIban();

	}

	// Este foi o método que percebemos que era redundante e mudamos o código para
	// não o ter de utilizar.
//	public void mbwayTransfer(String sourcePhoneNumber, String targetPhoneNumber, int amount)
//			throws SibsException, AccountException, OperationException, ServicesException, MbwayException {
//		this.getSibs().transfer(this.getIbanByPhoneNumber(sourcePhoneNumber),
//				this.getIbanByPhoneNumber(targetPhoneNumber), amount);
//
//	}

	public void clearMbwayAccounts() {
		this.mbwayAccounts.clear();
	}

	public Sibs getSibs() {
		return this.sibs;
	}
}