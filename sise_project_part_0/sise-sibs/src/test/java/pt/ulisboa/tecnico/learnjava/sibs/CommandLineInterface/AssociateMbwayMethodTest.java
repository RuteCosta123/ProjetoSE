package pt.ulisboa.tecnico.learnjava.sibs.CommandLineInterface;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank.AccountType;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.sibs.ComandLineInterface.Mbway;
import pt.ulisboa.tecnico.learnjava.sibs.ComandLineInterface.MbwayAccount;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.MbwayException;

public class AssociateMbwayMethodTest {

	private Bank bank;
	private Client client;
	private Mbway mbway = Mbway.getInstance();

	@Before
	public void setUp() throws BankException, ClientException, AccountException {

		this.bank = new Bank("CGD");
		this.client = new Client(this.bank, "Rute", "Costa", "123456789", null, "Rua das Libelinhas", 22);
		this.bank.addClient(this.client);
		this.bank.createAccount(AccountType.CHECKING, this.client, 100000, 0);

	}

	@Test
	public void success() throws MbwayException {

		MbwayAccount mbwayAccount = new MbwayAccount("CGDCK1", "123456789");

		assertTrue(mbwayAccount.getCode() != 0);
		assertTrue(this.mbway.checkExistingIban("CGDCK1"));
	}

	@Test
	public void associateMbwayNotValidIban() {
		try {
			new MbwayAccount("CGDCK2", "123456789");
			fail();
		} catch (MbwayException e) {
			assertTrue(!this.mbway.checkExistingIban("CGDCK2"));

		}
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
		this.client = null;
		this.bank = null;
		this.mbway.clearMbwayAccounts();

	}

}
