package ru.smirnovv;

/**
 * This exception indicates that the requested resource was not found.
 */
public class NotFoundException extends RuntimeException {
    /**
     * Constructs an instance with the specified detail message.
     *
     * @param message the detail message.
     */
    public NotFoundException(final String message) {
        super(message);
    }
}
