package suncertify.business;

public class AlreadyBookedException extends ServicesException {

	public AlreadyBookedException() {
		super();
	}

	public AlreadyBookedException(String message) {
		super(message);
	}
}
