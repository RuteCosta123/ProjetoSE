package pt.ulisboa.tecnico.learnjava.sibs.ComandLineInterface;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ServicesException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.MbwayException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class MbwayTransfer {
	private Mbway mbway;
	private MbwayAccount sourceMbwayAccount;
	private MbwayAccount targetMbwayAccount;

	public MbwayTransfer(String sourcePhoneNumber, String targetPhoneNumber, Integer amount)
			throws SibsException, AccountException, OperationException, ServicesException, MbwayException {
		this.mbway = Mbway.getInstance();
		this.sourceMbwayAccount = this.mbway.getMbwayAccount(sourcePhoneNumber);
		this.targetMbwayAccount = this.mbway.getMbwayAccount(targetPhoneNumber);

		if (this.canTransfer(this.sourceMbwayAccount, this.targetMbwayAccount, amount)) {

			try {
				this.mbway.getSibs().transfer(this.mbway.getIbanByPhoneNumber(sourcePhoneNumber),
						this.mbway.getIbanByPhoneNumber(targetPhoneNumber), amount);
				System.out.println("Transfer successful!");
			} catch (SibsException e) {
				System.out.println("Could not complete transfer!");
			}
		} else {
			System.out.println("Could not complete transfer!");
		}
	}

	private boolean canTransfer(MbwayAccount sourceMbwayAccount, MbwayAccount targetMbwayAccount, Integer amount)
			throws ServicesException {
		return this.sourceMbwayAccount.isActive() && this.targetMbwayAccount.isActive()
				&& this.mbway.getSibs().getServices().canWithdraw(sourceMbwayAccount.getIban(), amount)
				&& this.mbway.getSibs().getServices().canDeposit(targetMbwayAccount.getIban(), amount);
	}
}
