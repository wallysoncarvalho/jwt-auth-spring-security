package wally.info.exception;

public class UserExistsException extends RuntimeException{
	public UserExistsException(String message) {
		super(message);
	}
}
