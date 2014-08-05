package net.devcat.jmschannel.exceptions;

/**
 * Thrown when there is a problem with a JMSChannel. Mesage provides
 * details of the error that occurred.
 */
public class JMSChannelException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public JMSChannelException(final String message) {
        super(message);
    }
}

