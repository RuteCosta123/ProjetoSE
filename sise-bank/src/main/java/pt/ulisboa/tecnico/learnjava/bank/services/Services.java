package pt.ulisboa.tecnico.learnjava.bank.services;

import pt.ulisboa.tecnico.learnjava.bank.domain.Account;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ServicesException;

public class Services {
	public void deposit(String iban, int amount) throws AccountException, ServicesException {
		Account account = this.getAccountByIban(iban);

		account.deposit(amount);
	}

	public void withdraw(String iban, int amount) throws AccountException, ServicesException {
		Account account = this.getAccountByIban(iban);

		account.withdraw(amount);
	}

	public Account getAccountByIban(String iban) throws ServicesException {
		String code = iban.substring(0, 3);
		String accountId = iban.substring(3);

		Bank bank = Bank.getBankByCode(code);
		Account account = bank.getAccountByAccountId(accountId);

		if (bank == null || account == null) {
			throw new ServicesException();
		}

		return account;
	}

	public boolean canWithdraw(String sourceIban, int amount) throws ServicesException {
		if (this.getAccountByIban(sourceIban) == null || this.getAccountByIban(sourceIban).getBalance() < amount
				|| this.getAccountByIban(sourceIban).isInactive() || amount <= 0) {

			return false;
		}

		return true;
	}

	public boolean canDeposit(String targetIban, int amount) throws ServicesException {
		if (this.getAccountByIban(targetIban) == null || this.getAccountByIban(targetIban).isInactive()
				|| amount <= 0) {
			return false;
		}
		return true;
	}

}
