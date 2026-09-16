package az.ingress.exception;

public class UnprocessableEntityException extends RuntimeException {

  public UnprocessableEntityException(final String message, final Object... args) {
    super(message.formatted(args));
  }
}
