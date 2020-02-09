package pt.ulisboa.tecnico.learnjava.bank.domain;

public class CartaoCidadao {

	private String fullName;
	private final String nif;
	private String address;
	private int age;

	public CartaoCidadao(String fullName, String nif, String address, int age) {
		this.fullName = fullName;
		this.nif = nif;
		this.address = address;
		this.age = age;
	}

	public String getFirstName() {

		return this.fullName.split(" ")[0];
	}

	public String getLastName() {
		return this.fullName.split(" ")[this.fullName.length() - 1];
	}

	public String getNif() {
		return this.nif;
	}

	public String getAddress() {
		return this.address;
	}

	public int getAge() {
		return this.age;
	}
}
