package az.ingress.service;

import az.ingress.dao.CreditStorage;
import az.ingress.domain.Credit;
import az.ingress.exception.ExceptionMessage;
import az.ingress.exception.NotFoundException;

import java.math.BigDecimal;
import java.util.List;

public class CreditService {

  private final CustomerService customerService;
  private final OfferService offerService;
  private final CreditStorage creditStorage;

  public CreditService(final CustomerService customerService, final OfferService offerService, final CreditStorage creditStorage) {
    this.customerService = customerService;
    this.offerService = offerService;
    this.creditStorage = creditStorage;
  }

  public Credit create(final String pin, final BigDecimal requestedAmount) {
    final var customer = customerService.getByPin(pin);
    final var offers = offerService.generate(requestedAmount);
    final var credit = new Credit(creditStorage.nextId(), customer, requestedAmount).addOffers(offers);

    return creditStorage.save(credit);
  }

  public Credit accept(final Long creditId, final Long offerId) {
    final var credit = getById(creditId);
    final var acceptedCredit = credit.accept(offerId);
    return creditStorage.save(acceptedCredit);
  }

  public Credit reject(final Long creditId) {
    final var credit = getById(creditId);
    final var rejectedCredit = credit.reject();
    return creditStorage.save(rejectedCredit);
  }

  public Credit getById(final Long id) {
    return creditStorage.findById(id)
                        .orElseThrow(
                            () -> new NotFoundException(ExceptionMessage.CREDIT_NOT_FOUND, id));
  }

  public List<Credit> getAll() {
    return creditStorage.findAll();
  }
}
