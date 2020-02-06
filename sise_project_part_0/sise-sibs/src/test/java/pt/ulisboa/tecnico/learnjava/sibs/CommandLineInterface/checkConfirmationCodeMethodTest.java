package pt.ulisboa.tecnico.learnjava.sibs.CommandLineInterface;

import org.junit.After;
import org.junit.Before;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank.AccountType;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.sibs.ComandLineInterface.Mbway;
import pt.ulisboa.tecnico.learnjava.sibs.ComandLineInterface.MbwayAccount;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.MbwayException;

public class checkConfirmationCodeMethodTest {
	private Bank bank;
	private Client client;
	private MbwayAccount mbwayAccount;
	private Mbway mbway = Mbway.getInstance();

	@Before
	public void setUp() throws BankException, ClientException, AccountException, MbwayException {

		this.bank = new Bank("CGD");
		this.client = new Client(this.bank, "Rute", "Costa", "123456789", null, "Rua das Libelinhas", 22);
		this.bank.addClient(this.client);
		this.bank.createAccount(AccountType.CHECKING, this.client, 100000, 0);
		this.mbwayAccount = new MbwayAccount("CGDCK1", "123456789");

	}

//	@Test
//	public void success() throws MbwayException {
//
//		assertTrue(this.mbway.checkConfirmationCode("123456789", this.mbwayAccount.getCode()));
//
//	}
//
//	@Test
//	public void associateMbwayNotValidIban() throws MbwayException {
//		this.mbway.checkConfirmationCode("123456789", 000_000);
//		assertTrue(!this.mbway.checkConfirmationCode("123456789", 000_000));
//		assertTrue(000_000 != this.mbwayAccount.getCode());
//
//	}

	@After
	public void tearDown() {
		Bank.clearBanks();
		this.client = null;
		this.bank = null;
		this.mbwayAccount = null;
		this.mbway.clearMbwayAccounts();

	}
}
