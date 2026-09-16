package az.ingress.service;

import az.ingress.domain.Offer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static az.ingress.constants.OfferConstants.AMOUNT_SCALE;
import static az.ingress.constants.OfferConstants.HUNDRED;
import static az.ingress.constants.OfferConstants.INTEREST_SCALE;
import static az.ingress.constants.OfferConstants.MAX_INTEREST;
import static az.ingress.constants.OfferConstants.MAX_OFFER_COUNT;
import static az.ingress.constants.OfferConstants.MIN_INTEREST;
import static az.ingress.constants.OfferConstants.MIN_OFFER_COUNT;
import static az.ingress.constants.OfferConstants.MONTHS_IN_YEAR;
import static az.ingress.constants.OfferConstants.TERMS;
import static java.math.RoundingMode.HALF_UP;

public class OfferService {

  private final Random random = new Random();

  public List<Offer> generate(final BigDecimal requestedAmount) {
    final var amount = requestedAmount.setScale(AMOUNT_SCALE, HALF_UP);
    final var offers = new ArrayList<Offer>();

    for (final var term : randomTerms()) {
      offers.add(createOffer(offers.size() + 1L, amount, term));
    }

    return List.copyOf(offers);
  }

  private Offer createOffer(final Long id, final BigDecimal amount, final Integer term) {
    final var interest = randomInterest();
    final var monthlyPayment = monthlyPayment(amount, interest, term);

    return new Offer(id, term, amount, interest, monthlyPayment);
  }

  private List<Integer> randomTerms() {
    final var terms = new ArrayList<>(TERMS);
    Collections.shuffle(terms, random);

    final var count = random.nextInt(MIN_OFFER_COUNT, MAX_OFFER_COUNT + 1);
    return terms.subList(0, count).stream().sorted().toList();
  }

  private BigDecimal randomInterest() {
    return BigDecimal.valueOf(random.nextDouble(MIN_INTEREST, MAX_INTEREST))
                     .setScale(INTEREST_SCALE, HALF_UP);
  }

  private BigDecimal monthlyPayment(final BigDecimal amount, final BigDecimal interest, final Integer term) {
    final var months = BigDecimal.valueOf(term);
    final var totalInterest = amount.multiply(interest)
                              .multiply(months)
                              .divide(HUNDRED.multiply(MONTHS_IN_YEAR), AMOUNT_SCALE, HALF_UP);

    return amount.add(totalInterest).divide(months, AMOUNT_SCALE, HALF_UP);
  }
}
