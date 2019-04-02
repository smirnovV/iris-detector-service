package ru.smirnovv.person;

/**
 * This exception indicates that the name does not conform to the naming syntax.
 * The name must contain Latin characters, numbers, '-', '.',
 * start with a capital letter, no longer than 50 characters and not empty.
 */
public class InvalidNameException extends RuntimeException {
    /**
     * Constructs an instance with the specified detail message.
     *
     * @param message the detail message.
     */
    public InvalidNameException(final String message) {
        super(message);
    }
}
