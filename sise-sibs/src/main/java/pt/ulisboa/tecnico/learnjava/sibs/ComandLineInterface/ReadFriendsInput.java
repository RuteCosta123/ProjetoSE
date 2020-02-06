package pt.ulisboa.tecnico.learnjava.sibs.ComandLineInterface;

import java.util.HashMap;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.ServicesException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.MbwayException;

public class ReadFriendsInput {

	private Mbway mbway = Mbway.getInstance();
	private static HashMap<String, Integer> friends = new HashMap<>();

	private static String targetPhoneNumber;
	private static Integer targetAmountPaied;

	public ReadFriendsInput(Integer totalFriends, String phoneNumber, Integer amount)
			throws ServicesException, MbwayException {

		if (friends.size() == 1) {
			targetPhoneNumber = phoneNumber;
			targetAmountPaied = amount;

		}
		if (friends.size() >= totalFriends) {
			System.out.println("Oh no! Too many friends.");

		} else {
			if (!this.existingMbwayAccount(phoneNumber)) {
				System.out.println("Oh no, this friend does not have an MbwayAccount");

			} else if (!this.validMbwayAccount(phoneNumber, amount)) {
				System.out.println("Oh on, one of your friends does not have money to pay!");

			} else {
				friends.put(phoneNumber, amount);

			}

		}
	}

	public static int getNumberFriends() {
		return friends.size();
	}

	public static HashMap<String, Integer> getFriendsInfo() {
		return friends;
	}

	public boolean existingMbwayAccount(String phoneNumber) throws ServicesException, MbwayException {
		return this.mbway.getMbwayAccount(phoneNumber) != null;
	}

	public boolean validMbwayAccount(String phoneNumber, int amount) throws ServicesException, MbwayException {
		return this.mbway.getMbwayAccount(phoneNumber).isActive()
				&& this.mbway.getSibs().getServices().canWithdraw(this.mbway.getIbanByPhoneNumber(phoneNumber), amount);
	}

	public static String getTargetPhoneNumber() {
		return targetPhoneNumber;
	}

//Este foi o método final criado para cumprir a Guideline Write Short Units of Code. 
	public static void resetTargetInfo() {
		targetPhoneNumber = null;
		targetAmountPaied = null;
	}

	public static Integer getTargetAmountPaied() {
		return targetAmountPaied;
	}
}
