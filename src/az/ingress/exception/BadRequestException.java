package az.ingress.exception;

public class BadRequestException extends RuntimeException {

  public BadRequestException(final String message, final Object... args) {
    super(message.formatted(args));
  }
}
