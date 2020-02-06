package pt.ulisboa.tecnico.learnjava.sibs.ComandLineInterface;

import java.util.HashMap;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ServicesException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.MbwayException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class MbwaySplitBill {

	private Mbway mbway;
	private HashMap<String, Integer> friendsInfo;

	public MbwaySplitBill(Integer numberOfFriends, Integer totalAmount) throws NumberFormatException, SibsException,
			AccountException, OperationException, ServicesException, MbwayException {

		this.mbway = Mbway.getInstance();
		this.friendsInfo = ReadFriendsInput.getFriendsInfo();

		if (!this.checkPaymentMoney(totalAmount)) {
			System.out.println("Something is wrong. Did you set the bill amount right?");
		} else {
			String targetPhoneNumber = ReadFriendsInput.getTargetPhoneNumber();

			for (String phoneNumber : this.friendsInfo.keySet()) {

				this.mbway.mbwayTransfer(phoneNumber, targetPhoneNumber, this.friendsInfo.get(phoneNumber));
				System.out.println(this.getFirstName(phoneNumber) + " payed successfully!");
			}

		}

		ReadFriendsInput.getFriendsInfo().clear();
		ReadFriendsInput.resetTargetInfo();
	}

	// Este método (getPaymentMoney) está a melhorar o código de acordo com a
	// guideline Write Short Units of Code.

	private boolean checkPaymentMoney(Integer totalAmount) {
		int totalPaymentMoney = 0;

		for (Integer amount : this.friendsInfo.values()) {
			totalPaymentMoney += amount;
		}

		if (totalPaymentMoney != totalAmount) {
			return true;
		} else {
			return false;
		}

	}

	private String getFirstName(String phoneNumber) throws ServicesException, MbwayException {
		String friendName = this.mbway.getSibs().getServices()
				.getAccountByIban(this.mbway.getIbanByPhoneNumber(phoneNumber)).getClient().getFirstName();
		return friendName;
	}
}
