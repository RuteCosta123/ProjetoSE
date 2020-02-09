package pt.ulisboa.tecnico.learnjava.sibs.ComandLineInterface;

import java.util.ArrayList;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ServicesException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.MbwayException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class MbwaySplitBill {

	public MbwaySplitBill(Integer numberOfFriends, Integer totalAmount, Mbway mbway, ArrayList<Friend> listOfFriends)
			throws NumberFormatException, SibsException, AccountException, OperationException, ServicesException,
			MbwayException {

		if (listOfFriends.size() >= numberOfFriends) {
			System.out.println("Oh no! Too many friends.");

		} else if (listOfFriends.size() < numberOfFriends) {
			System.out.println("Oh no! One friend is missing.");
		}

		else if (!this.checkPaymentMoney(totalAmount, listOfFriends)) {
			System.out.println("Something is wrong. Did you set the bill amount right?");
		}

		else {
			String targetPhoneNumber = listOfFriends.get(0).getPhoneNumber();

			for (int i = 1; i < listOfFriends.size(); i++) {
				Friend sourceFriend = listOfFriends.get(i);

				new MbwayTransfer(sourceFriend.getPhoneNumber(), targetPhoneNumber, sourceFriend.getAmount(), mbway);
				System.out.println(this.getFirstName(sourceFriend.getPhoneNumber(), mbway) + " payed successfully!");
			}
		}

		listOfFriends.clear();
	}

	// Este método (getPaymentMoney) está a melhorar o código de acordo com a
	// guideline Write Short Units of Code.

	private boolean checkPaymentMoney(Integer totalAmount, ArrayList<Friend> listOfFriends) {
		int totalPaymentMoney = 0;

		for (Friend friend : listOfFriends) {

			totalPaymentMoney += friend.getAmount();
		}

		if (totalPaymentMoney != totalAmount) {
			return true;
		} else {
			return false;
		}

	}

	private String getFirstName(String phoneNumber, Mbway mbway) throws ServicesException, MbwayException {
		String friendName = mbway.getSibs().getServices().getAccountByIban(mbway.getIbanByPhoneNumber(phoneNumber))
				.getClient().getFirstName();
		return friendName;
	}
}
