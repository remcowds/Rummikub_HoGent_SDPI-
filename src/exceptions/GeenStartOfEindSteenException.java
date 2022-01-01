package exceptions;

public class GeenStartOfEindSteenException extends RuntimeException{

	public GeenStartOfEindSteenException() {
		super("serie--> moet start of eindsteen zijn naar werkveld");
		// TODO Auto-generated constructor stub
	}

	public GeenStartOfEindSteenException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public GeenStartOfEindSteenException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public GeenStartOfEindSteenException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
