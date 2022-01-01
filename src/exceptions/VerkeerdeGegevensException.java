package exceptions;

public class VerkeerdeGegevensException extends RuntimeException{

	public VerkeerdeGegevensException() {
		super("verkeerde gegevens");
		// TODO Auto-generated constructor stub
	}

	public VerkeerdeGegevensException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public VerkeerdeGegevensException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public VerkeerdeGegevensException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
}
