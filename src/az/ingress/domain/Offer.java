package az.ingress.domain;

import java.math.BigDecimal;

public record Offer(Long id, Integer term, BigDecimal amount, BigDecimal interest, BigDecimal monthlyPayment) {
}
