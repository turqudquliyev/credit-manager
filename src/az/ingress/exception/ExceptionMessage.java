package az.ingress.exception;

public final class ExceptionMessage {

  public static final String INVALID_PIN =
      "Invalid PIN '%s', it must be 7 letters or digits, for example 5AB1234";
  public static final String INVALID_PHONE_NUMBER =
      "Invalid phone number '%s', for example +994501234567 or 0501234567";
  public static final String INVALID_FULL_NAME =
      "Invalid full name '%s', name and surname are required, for example Elvin Memmedov";

  public static final String CUSTOMER_NOT_FOUND = "Customer not found with PIN: %s";
  public static final String CREDIT_NOT_FOUND = "Credit not found: %s";
  public static final String OFFER_NOT_FOUND = "Offer not found: %s";

  public static final String CUSTOMER_ALREADY_EXISTS = "Customer already exists with PIN: %s";
  public static final String CREDIT_IS_NOT_DRAFT =
      "Credit is already in %s status and cannot be changed";

  private ExceptionMessage() {
  }
}
