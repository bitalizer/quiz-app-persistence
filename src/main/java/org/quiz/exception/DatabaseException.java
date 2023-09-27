package org.quiz.exception;

/**
 * Exception class for encapsulating database-related errors in the DAO layer.
 */
public class DatabaseException extends RuntimeException {

  /**
   * Constructs a new {@code DatabaseException} with the specified detail message.
   *
   * @param message The detail message (which is saved for later retrieval by the {@link #getMessage()} method)
   */
  public DatabaseException(String message) {
    super(message);
  }

  /**
   * Constructs a new {@code DatabaseException} with the specified detail message and cause.
   *
   * @param message The detail message (which is saved for later retrieval by the {@link #getMessage()} method)
   * @param cause   The cause (which is saved for later retrieval by the {@link #getCause()} method)
   */
  public DatabaseException(String message, Throwable cause) {
    super(message, cause);
  }
}