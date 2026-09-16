package az.ingress.constants;

public final class RegexConstants {

  public static final String PIN = "^[A-Za-z0-9]{7}$";
  public static final String PHONE_NUMBER = "^(\\+994|0)(10|50|51|55|60|70|77|99)\\d{7}$";
  public static final String FULL_NAME = "^\\p{L}{2,}([ -]\\p{L}{2,})+$";
  public static final String AMOUNT = "^\\d{1,9}([.,]\\d{1,2})?$";

  private RegexConstants() {
  }
}
