package az.ingress.constants;

import java.math.BigDecimal;
import java.util.List;

public final class OfferConstants {

  public static final List<Integer> TERMS = List.of(6, 12, 18, 24, 36, 48);

  public static final int MIN_OFFER_COUNT = 3;
  public static final int MAX_OFFER_COUNT = 5;

  public static final double MIN_INTEREST = 15.0;
  public static final double MAX_INTEREST = 26.0;

  public static final BigDecimal MONTHS_IN_YEAR = BigDecimal.valueOf(12);
  public static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

  public static final int AMOUNT_SCALE = 2;
  public static final int INTEREST_SCALE = 1;

  private OfferConstants() {
  }
}
