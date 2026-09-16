package az.ingress.console;

import static az.ingress.constants.RegexConstants.AMOUNT;
import static az.ingress.constants.RegexConstants.FULL_NAME;
import static az.ingress.constants.RegexConstants.PHONE_NUMBER;
import static az.ingress.constants.RegexConstants.PIN;

import az.ingress.domain.Credit;
import az.ingress.domain.Customer;
import az.ingress.domain.Offer;
import az.ingress.exception.BadRequestException;
import az.ingress.exception.NotFoundException;
import az.ingress.exception.UnprocessableEntityException;
import az.ingress.service.CreditService;
import az.ingress.service.CustomerService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ConsoleApplication {

  private final Scanner scanner = new Scanner(System.in);
  private final CustomerService customerService;
  private final CreditService creditService;

  public ConsoleApplication(final CustomerService customerService, final CreditService creditService) {
    this.customerService = customerService;
    this.creditService = creditService;
  }

  public void run() {
    System.out.println("=== CREDIT MANAGER ===");

    while (true) {
      printMenu();
      final var choice = ask("Your choice");

      if (choice.equals("0")) {
        break;
      }

      try {
        execute(choice);
      } catch (BadRequestException | NotFoundException | UnprocessableEntityException ex) {
        System.out.println("Error: " + ex.getMessage());
      }
    }

    System.out.println("Goodbye!");
  }

  private void printMenu() {
    System.out.println("""
        
        ------------------------------
          1) Create customer
          2) List customers
          3) Apply for credit
          4) List credits
          5) Show credit
          6) Accept offer
          7) Reject credit
          0) Exit
        ------------------------------""");
  }

  private void execute(final String choice) {
    switch (choice) {
      case "1" -> createCustomer();
      case "2" -> printCustomers(customerService.getAll());
      case "3" -> createCredit();
      case "4" -> printCredits(creditService.getAll());
      case "5" -> print(creditService.getById(askLong("Credit ID", "1")));
      case "6" -> acceptOffer();
      case "7" -> rejectCredit();
      default -> System.out.println("No such option: " + choice);
    }
  }

  private void createCustomer() {
    System.out.println("-- New customer --");
    final var pin = ask("PIN", "5AB1234", PIN, "PIN must be exactly 7 letters or digits.");
    final var phoneNumber = ask("Phone number", "+994501234567", PHONE_NUMBER,
        "Phone number must start with +994 or 0 and contain a valid operator code.");
    final var fullName =
        ask("Full name", "Elvin Memmedov", FULL_NAME, "Enter both name and surname.");

    final var customer = customerService.create(pin, phoneNumber, fullName);
    System.out.println("Customer created:");
    print(customer);
  }

  private void createCredit() {
    System.out.println("-- New credit application --");
    final var pin =
        ask("Customer PIN", "5AB1234", PIN, "PIN must be exactly 7 letters or digits.");
    final var requestedAmount = askAmount("Requested amount", "1500");

    final var credit = creditService.create(pin, requestedAmount);
    System.out.printf("Application created, the bank prepared %d offers:%n",
        credit.getOffers().size());
    print(credit);
  }

  private void acceptOffer() {
    System.out.println("-- Accept an offer --");
    final var credit = creditService.getById(askLong("Credit ID", "1"));
    print(credit);

    final var offerId = askLong("Offer ID to accept", "2");
    final var acceptedCredit = creditService.accept(credit.getId(), offerId);

    System.out.println("Offer accepted:");
    print(acceptedCredit);
  }

  private void rejectCredit() {
    System.out.println("-- Reject a credit --");
    final var credit = creditService.getById(askLong("Credit ID", "1"));
    print(credit);

    final var rejectedCredit = creditService.reject(credit.getId());

    System.out.println("Credit rejected:");
    print(rejectedCredit);
  }

  private void printCustomers(final List<Customer> customers) {
    if (customers.isEmpty()) {
      System.out.println("No customers yet.");
      return;
    }
    System.out.println("Customers (" + customers.size() + "):");
    customers.forEach(this::print);
  }

  private void printCredits(final List<Credit> credits) {
    if (credits.isEmpty()) {
      System.out.println("No credit applications yet.");
      return;
    }
    System.out.println("Credits (" + credits.size() + "):");
    credits.forEach(this::print);
  }

  private void print(final Customer customer) {
    System.out.printf("  PIN %s | %s | %s%n", customer.pin(), customer.fullName(),
        customer.phoneNumber());
  }

  private void print(final Credit credit) {
    System.out.printf("  Credit #%d | %s | requested: %s AZN | status: %s%n",
        credit.getId(), credit.getCustomer().fullName(), credit.getRequestedAmount(),
        credit.getStatus());

    if (credit.getOffers().isEmpty()) {
      System.out.println("    (no offers)");
      return;
    }
    credit.getOffers().forEach(offer -> print(offer, offer.equals(credit.getAcceptedOffer())));
  }

  private void print(final Offer offer, final boolean accepted) {
    System.out.printf("    %s Offer #%d | %2d months | %10s AZN | %4s%% | monthly %9s AZN%n",
        accepted ? "->" : "  ", offer.id(), offer.term(), offer.amount(), offer.interest(),
        offer.monthlyPayment());
  }

  private String ask(final String label) {
    while (true) {
      System.out.print(label + ": ");
      if (!scanner.hasNextLine()) {
        System.out.println("\nGoodbye!");
        System.exit(0);
      }

      final var value = scanner.nextLine().trim().replaceAll("\\s+", " ");
      if (!value.isEmpty()) {
        return value;
      }
      System.out.println("Value cannot be empty.");
    }
  }

  private String ask(final String label, final String example) {
    return ask(label + " (e.g. " + example + ")");
  }

  private String ask(final String label, final String example, final String regex, final String hint) {
    while (true) {
      final var value = ask(label, example);
      if (value.matches(regex)) {
        return value;
      }
      System.out.println(hint);
    }
  }

  private Long askLong(final String label, final String example) {
    while (true) {
      final var value = ask(label, example);
      try {
        return Long.parseLong(value);
      } catch (final NumberFormatException ex) {
        System.out.println("Enter a whole number, for example " + example);
      }
    }
  }

  private BigDecimal askAmount(final String label, final String example) {
    while (true) {
      final var value = ask(label, example, AMOUNT,
          "Enter a valid amount, for example " + example + " or " + example + ".50");

      final var amount = new BigDecimal(value.replace(',', '.'));
      if (amount.signum() > 0) {
        return amount;
      }
      System.out.println("Value must be greater than zero.");
    }
  }
}
