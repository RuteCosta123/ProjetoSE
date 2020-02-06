//package pt.ulisboa.tecnico.learnjava.sibs.ComandLineInterface;
//
//import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
//import pt.ulisboa.tecnico.learnjava.bank.exceptions.ServicesException;
//import pt.ulisboa.tecnico.learnjava.sibs.exceptions.MbwayException;
//import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
//import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;
//
//public class MbwayPayment {
//
//	private Mbway mbway;
//	private Integer amount;
//	private String phoneNumber;
//	private int amountPayed;
//
//	public MbwayPayment(String phoneNumber, Integer amount)
//			throws SibsException, AccountException, OperationException, ServicesException, MbwayException {
//		this.phoneNumber = phoneNumber;
//		this.amount = amount;
//		this.amountPayed = 0;
//		this.mbway = Mbway.getInstance();
////		try {
//		this.mbway.getSibs().payment(null, this.mbway.getIbanByPhoneNumber(phoneNumber), amount);
//		this.amountPayed = amount;
//		String nome = this.mbway.getSibs().getServices().getAccountByIban(this.mbway.getIbanByPhoneNumber(phoneNumber))
//				.getClient().getFirstName();
//		System.out.println(nome + " payed successfully!");
////		} catch (MbwayException e) {
////			System.out.println("Oh no! One of your friends does not have a Mbway Account");
////		} catch (OperationException e) {
////			System.out.println("Oh no! One of your friends does not have money to pay!");
////		}
//	}
//
//	public Integer getAmountPayed() {
//		return this.amountPayed;
//	}
//
//}
