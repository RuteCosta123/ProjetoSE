package pt.ulisboa.tecnico.learnjava.sibs.ComandLineInterface;

import java.util.Random;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.ServicesException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.MbwayException;

public class MbwayAccount {
	private String iban;
	private String phoneNumber;
	private Integer code;
	private Services service;
	private boolean status;

	public MbwayAccount(String iban, String phoneNumber, Mbway mbway) throws MbwayException {
		this.service = mbway.getSibs().getServices();
		this.status = false;

		if (this.checkParameters(iban, phoneNumber, mbway)) {

			this.iban = iban;
			this.phoneNumber = phoneNumber;
			this.code = this.generateRandomNumber();
			mbway.addMbwayAccount(this);
			System.out.println("Code: " + this.code + "(don't share with anyone)");
		} else {
			System.out.println("IBAN not valid");
		}
	}

	private boolean checkParameters(String iban, String phoneNumber, Mbway mbway) {
		if (this.phoneNumber == null || this.iban == null || this.phoneNumber.length() != 9 ? false : true)
			;
		{
			try {
				this.service.getAccountByIban(iban);
				return mbway.checkExistingIban(iban) ? false : true;

			} catch (ServicesException e) {
				return false;
			}
		}
	}

//	private boolean checkIban(String iban, Mbway mbway) {
//		try {
//			this.service.getAccountByIban(iban);
//		} catch (ServicesException e) {
//			return false;
//
//		}
//
//		if (mbway.checkExistingIban(iban)) {
//			return false;
//		}
//		return true;
//	}

	public void activateAccount() {
		this.status = true;
	}

	public boolean isActive() {
		return this.status;
	}

	public String getIban() {
		return this.iban;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public Integer getCode() {
		return this.code;
	}

	private int generateRandomNumber() {
		Random random = new Random();
		return random.ints(111111, 999999).findFirst().getAsInt();
	}

}
