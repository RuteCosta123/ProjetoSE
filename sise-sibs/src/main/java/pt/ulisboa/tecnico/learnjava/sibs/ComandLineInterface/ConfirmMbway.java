package pt.ulisboa.tecnico.learnjava.sibs.ComandLineInterface;

public class ConfirmMbway {

	private Mbway mbway;
	private String phoneNumber;
	private Integer code;
	private boolean associationConfirmation;

	public ConfirmMbway(String phoneNumber, Integer code) {
		this.phoneNumber = phoneNumber;
		this.code = code;
		this.mbway = Mbway.getInstance();
		this.associationConfirmation = this.checkConfirmationCode();
		if (this.isAssociationConfirmed()) {
			this.mbway.getMbwayAccount(phoneNumber).activateAccount();
			System.out.println("MBWay Association Confirmed Successfully!");
		} else {
			System.out.println("Wrong confirmation code!");
		}
	}

	public boolean checkConfirmationCode() {
		MbwayAccount mbwayAccount = this.mbway.getMbwayAccount(this.phoneNumber);
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