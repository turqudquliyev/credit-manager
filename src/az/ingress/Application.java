package az.ingress;

import az.ingress.console.ConsoleApplication;
import az.ingress.dao.CreditStorage;
import az.ingress.dao.CustomerStorage;
import az.ingress.service.CreditService;
import az.ingress.service.CustomerService;
import az.ingress.service.OfferService;

public final class Application {

  private final ConsoleApplication consoleApplication;

  public Application() {
    final var customerStorage = new CustomerStorage();
    final var creditStorage = new CreditStorage();

    final var customerService = new CustomerService(customerStorage);
    final var offerService = new OfferService();
    final var creditService = new CreditService(customerService, offerService, creditStorage);

    this.consoleApplication = new ConsoleApplication(customerService, creditService);
  }

  public static void main(final String[] args) {
    final var application = new Application();
    application.consoleApplication.run();
  }
}
