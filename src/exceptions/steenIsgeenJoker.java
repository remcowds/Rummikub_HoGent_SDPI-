package exceptions;

public class steenIsgeenJoker extends RuntimeException {
	public steenIsgeenJoker() {
		super("Steen is geen Joker");
		// TODO Auto-generated constructor stub
	}

	public steenIsgeenJoker(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public steenIsgeenJoker(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public steenIsgeenJoker(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}