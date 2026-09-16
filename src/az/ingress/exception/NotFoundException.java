package az.ingress.exception;

public class NotFoundException extends RuntimeException {

  public NotFoundException(final String message, final Object... args) {
    super(message.formatted(args));
  }
}
