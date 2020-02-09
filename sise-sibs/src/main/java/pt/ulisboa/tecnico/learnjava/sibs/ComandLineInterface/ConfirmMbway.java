package pt.ulisboa.tecnico.learnjava.sibs.ComandLineInterface;

public class ConfirmMbway {

	private String phoneNumber;
	private Integer code;
	private boolean associationConfirmation;

	public ConfirmMbway(String phoneNumber, Integer code, Mbway mbway) {
		this.phoneNumber = phoneNumber;
		this.code = code;
		this.associationConfirmation = this.checkConfirmationCode(mbway);
		if (this.isAssociationConfirmed()) {
			mbway.getMbwayAccount(phoneNumber).activateAccount();
			System.out.println("MBWay Association Confirmed Successfully!");
		} else {
			System.out.println("Wrong confirmation code!");
		}
	}

	public boolean checkConfirmationCode(Mbway mbway) {
		MbwayAccount mbwayAccount = mbway.getMbwayAccount(this.phoneNumber);
		if (mbwayAccount == null || (!mbwayAccount.getCode().equals(this.code))) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isAssociationConfirmed() {
		return this.associationConfirmation;
	}
}