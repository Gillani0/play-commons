package eu.play_project.play_commons.eventtypes.eventvalidation;

public class InvalidEventException extends Exception {

	private static final long serialVersionUID = 100L;

	public InvalidEventException(String message) {
		super(message);
	}
}
