package az.ingress.dao;

import az.ingress.domain.Credit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CreditStorage {

  private final Map<Long, Credit> credits = new LinkedHashMap<>();

  private long nextId = 1;

  public Long nextId() {
    return nextId++;
  }

  public Credit save(final Credit credit) {
    credits.put(credit.getId(), credit);
    return credit;
  }

  public Optional<Credit> findById(final Long id) {
    return Optional.ofNullable(credits.get(id));
  }

  public List<Credit> findAll() {
    return List.copyOf(credits.values());
  }
}
