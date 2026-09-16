package az.ingress.service;

import static az.ingress.constants.RegexConstants.FULL_NAME;
import static az.ingress.constants.RegexConstants.PHONE_NUMBER;
import static az.ingress.constants.RegexConstants.PIN;
import static az.ingress.exception.ExceptionMessage.CUSTOMER_ALREADY_EXISTS;
import static az.ingress.exception.ExceptionMessage.CUSTOMER_NOT_FOUND;
import static az.ingress.exception.ExceptionMessage.INVALID_FULL_NAME;
import static az.ingress.exception.ExceptionMessage.INVALID_PHONE_NUMBER;
import static az.ingress.exception.ExceptionMessage.INVALID_PIN;

import az.ingress.dao.CustomerStorage;
import az.ingress.domain.Customer;
import az.ingress.exception.BadRequestException;
import az.ingress.exception.NotFoundException;
import az.ingress.exception.UnprocessableEntityException;
import java.util.List;

public class CustomerService {

  private final CustomerStorage customerStorage;

  public CustomerService(final CustomerStorage customerStorage) {
    this.customerStorage = customerStorage;
  }

  public Customer create(final String pin, final String phoneNumber, final String fullName) {
    validate(pin, phoneNumber, fullName);

    if (customerStorage.existsByPin(pin)) {
      throw new UnprocessableEntityException(CUSTOMER_ALREADY_EXISTS, pin);
    }

    return customerStorage.save(new Customer(pin, fullName, phoneNumber));
  }

  public Customer getByPin(final String pin) {
    return customerStorage.findByPin(pin)
                          .orElseThrow(() -> new NotFoundException(CUSTOMER_NOT_FOUND, pin));
  }

  public List<Customer> getAll() {
    return customerStorage.findAll();
  }

  private void validate(final String pin, final String phoneNumber, final String fullName) {
    if (!pin.matches(PIN)) {
      throw new BadRequestException(INVALID_PIN, pin);
    }
    if (!phoneNumber.matches(PHONE_NUMBER)) {
      throw new BadRequestException(INVALID_PHONE_NUMBER, phoneNumber);
    }
    if (!fullName.matches(FULL_NAME)) {
      throw new BadRequestException(INVALID_FULL_NAME, fullName);
    }
  }
}
