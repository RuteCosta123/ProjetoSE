package pt.ulisboa.tecnico.learnjava.sibs.ComandLineInterface;

public class ReadEndInput {

	public ReadEndInput(Integer totalFriends) {
		if (ReadFriendsInput.getNumberFriends() < totalFriends) {
			System.out.println("Oh no! One friend is missing.");
		}

	}

}
