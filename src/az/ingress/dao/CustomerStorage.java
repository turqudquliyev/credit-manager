package az.ingress.dao;

import az.ingress.domain.Customer;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class CustomerStorage {

  private final Map<String, Customer> customers = new LinkedHashMap<>();

  public Customer save(final Customer customer) {
    customers.put(customer.pin(), customer);
    return customer;
  }

  public Optional<Customer> findByPin(final String pin) {
    return Optional.ofNullable(customers.get(pin));
  }

  public boolean existsByPin(final String pin) {
    return customers.containsKey(pin);
  }

  public List<Customer> findAll() {
    return List.copyOf(customers.values());
  }
}
