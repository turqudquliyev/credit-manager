package az.ingress.domain;

import az.ingress.exception.ExceptionMessage;
import az.ingress.exception.NotFoundException;
import az.ingress.exception.UnprocessableEntityException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Credit {

  private final Long id;
  private final Customer customer;
  private final BigDecimal requestedAmount;
  private final CreditStatus status;
  private final Offer acceptedOffer;
  private final List<Offer> offers;

  public Credit(final Long id, final Customer customer, final BigDecimal requestedAmount) {
    this(id, customer, requestedAmount, CreditStatus.DRAFT, null, List.of());
  }

  private Credit(final Long id,
                 final Customer customer,
                 final BigDecimal requestedAmount,
                 final CreditStatus status,
                 final Offer acceptedOffer,
                 final List<Offer> offers) {
    this.id = id;
    this.customer = customer;
    this.requestedAmount = requestedAmount;
    this.status = status;
    this.acceptedOffer = acceptedOffer;
    this.offers = List.copyOf(offers);
  }

  public Credit addOffers(final List<Offer> newOffers) {
    requireDraft();
    final var updatedOffers = new ArrayList<>(offers);
    updatedOffers.addAll(newOffers);
    return new Credit(id, customer, requestedAmount, status, acceptedOffer, updatedOffers);
  }

  public Credit accept(final Long offerId) {
    requireDraft();
    return new Credit(id, customer, requestedAmount, CreditStatus.ACCEPTED, findOffer(offerId),
        offers);
  }

  public Credit reject() {
    requireDraft();
    return new Credit(id, customer, requestedAmount, CreditStatus.REJECTED, acceptedOffer, offers);
  }

  public Long getId() {
    return id;
  }

  public Customer getCustomer() {
    return customer;
  }

  public BigDecimal getRequestedAmount() {
    return requestedAmount;
  }

  public CreditStatus getStatus() {
    return status;
  }

  public Offer getAcceptedOffer() {
    return acceptedOffer;
  }

  public List<Offer> getOffers() {
    return List.copyOf(offers);
  }

  private Offer findOffer(final Long offerId) {
    return offers.stream()
                 .filter(offer -> offer.id().equals(offerId))
                 .findFirst()
                 .orElseThrow(
                     () -> new NotFoundException(ExceptionMessage.OFFER_NOT_FOUND, offerId));
  }

  private void requireDraft() {
    if (status != CreditStatus.DRAFT) {
      throw new UnprocessableEntityException(ExceptionMessage.CREDIT_IS_NOT_DRAFT, status);
    }
  }

  @Override
  public boolean equals(final Object o) {
    if (!(o instanceof final Credit credit)) {
      return false;
    }
    return Objects.equals(id, credit.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "Credit{" +
        "id=" + id +
        ", customer=" + customer +
        ", requestedAmount=" + requestedAmount +
        ", status=" + status +
        ", acceptedOffer=" + acceptedOffer +
        ", offers=" + offers +
        '}';
  }
}
