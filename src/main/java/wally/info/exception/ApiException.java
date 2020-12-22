package wally.info.exception;

public class ApiException extends RuntimeException{
	private final String message;

	public ApiException(String message) {
		this.message = message;
	}
}
