package pt.ulisboa.tecnico.learnjava.sibs.ComandLineInterface;

import java.util.Scanner;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank.AccountType;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ServicesException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.MbwayException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class ControlerMain {

	public static void main(String[] args) throws MbwayException, BankException, ClientException, AccountException,
			SibsException, OperationException, ServicesException {

		Mbway mbway = Mbway.getInstance();
		Bank bank = new Bank("CGD");
		Bank bank1 = new Bank("CTT");
		Client client1 = new Client(bank, "Rute", "Costa", "123456789", null, "Rua das Libelinhas", 23);
		Client client2 = new Client(bank, "Ines", "Agulheiro", "123000789", null, "Rua das Borboletas", 24);
		Client client3 = new Client(bank, "Miguel", "Carreto", "123456000", null, "Rua das Formigas", 23);
		Client client4 = new Client(bank, "Duarte", "Matos", "000456789", null, "Rua das Carochinhas", 25);
		Client client5 = new Client(bank1, "Rafael", "Matos", "000056789", null, "Rua das Carochinhas", 25);
		bank1.addClient(client5);
		bank.addClient(client1);
		bank.addClient(client2);
		bank.addClient(client3);
		bank.addClient(client4);
		bank1.createAccount(AccountType.CHECKING, client5, 1000, 0);
		bank.createAccount(AccountType.CHECKING, client1, 1000, 0);
		bank.createAccount(AccountType.CHECKING, client2, 1000, 0);
		bank.createAccount(AccountType.CHECKING, client3, 1000, 0);
		bank.createAccount(AccountType.CHECKING, client4, 1000, 0);
		MbwayAccount mbwayAccount;
		ConfirmMbway confirmMbway;
		MbwayTransfer mbwayTransfer;
		MbwaySplitBill mbwaySplitBill;

		while (true) {

			Scanner input = new Scanner(System.in);
			System.out.println("Please insert input");

			String command = input.nextLine();
			String[] Comando = command.split(" ");

//  associate-mbway CTTCK1 977654321
//	associate-mbway CGDCK2 987654321
//	associate-mbway CGDCK3 123456789
//	associate-mbway CGDCK4 456789123
//	associate-mbway CGDCK5 789123456
//  confirm-mbway 987654321 709033
//	confirm-mbway 123456789 709033
// 	confirm-mbway 456789123 709033
//	confirm-mbway 789123456 709033		
//	mbway-transfer 987654321 123456789 1000
//	mbway-split-bill 2 150
//	friend 987654321 100
//	friend 123456789 50
//	friend 456789123 50
//	end

			switch (Comando[0]) {
			case ("associate-mbway"):
				mbwayAccount = new MbwayAccount(Comando[1], Comando[2]);
				break;
			case ("confirm-mbway"):
				confirmMbway = new ConfirmMbway(Comando[1], Integer.parseInt(Comando[2]));
				break;
			case ("mbway-transfer"):
				mbwayTransfer = new MbwayTransfer(Comando[1], Comando[2], Integer.parseInt(Comando[3]));
				break;
			case ("mbway-split-bill"):
				boolean keepGoing = true;
				System.out.println("Please insert friend");
				while (keepGoing) {

					String dividirConta = input.nextLine();
					String[] friend = dividirConta.split(" ");

					switch (friend[0]) {
					case ("friend"):
						new ReadFriendsInput(Integer.parseInt(Comando[1]), friend[1], Integer.parseInt(friend[2]));
						break;

					case ("end"):
						new ReadEndInput(Integer.parseInt(Comando[1]));
						keepGoing = false;
						break;

					default:
						System.out.println("Input not valid, please insert friend!");
					}
				}
				mbwaySplitBill = new MbwaySplitBill(Integer.parseInt(Comando[1]), Integer.parseInt(Comando[2]));
				break;

			case ("exit"):
				System.exit(0);
				break;
			default:
				System.out.print("Invalid comand!");
				break;
			}

		}

	}

}
