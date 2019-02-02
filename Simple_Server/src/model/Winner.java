package model;

public class Winner {

	private String name;
	private int playerScore;
	private int computerScore;

	public Winner(String name, int playerScore, int computerScore) {
		this.name = name;
		this.computerScore = computerScore;
		this.playerScore = playerScore;
	}

	public Winner(String postMessage) throws MessageFormatException {
		int index;
		
		try {
			this.name = postMessage.substring(0, (index = postMessage.indexOf(',')));
			this.playerScore = new Integer(postMessage.substring(index + 1, 
					(index = postMessage.indexOf(',', index + 1))));
			this.computerScore = new Integer(postMessage.substring(index + 1,
					postMessage.indexOf(';')));
		} catch (NumberFormatException | NullPointerException | StringIndexOutOfBoundsException e){
			throw new MessageFormatException();
		}
	}
	
	public String getName() {
		return name;
	}
	
	public String getScore() {
		return playerScore + " - " + computerScore;
	}

	public String toString() {
		return name + "," + playerScore + "," + computerScore + ";";
	}

	public boolean equals(Object other) {
		if (other instanceof Winner) {
			return this.name.equals(((Winner) other).name) && this.playerScore == ((Winner) other).playerScore
					&& this.computerScore == ((Winner) other).playerScore;
		}
		return false;
	}
}
