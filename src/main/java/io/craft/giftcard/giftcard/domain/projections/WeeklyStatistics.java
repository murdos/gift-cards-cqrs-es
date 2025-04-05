package io.craft.giftcard.giftcard.domain.projections;

import io.craft.giftcard.giftcard.domain.Amount;
import io.craft.giftcard.giftcard.domain.events.GifCardExhausted;
import io.craft.giftcard.giftcard.domain.events.GiftCardDeclared;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import io.craft.giftcard.giftcard.domain.events.PaidAmount;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

public record WeeklyStatistics(Map<DayOfWeek, Amount> values) {
  public static final WeeklyStatistics EMPTY = new WeeklyStatistics(
    Map.of(
      DayOfWeek.MONDAY,
      Amount.ZERO,
      DayOfWeek.TUESDAY,
      Amount.ZERO,
      DayOfWeek.WEDNESDAY,
      Amount.ZERO,
      DayOfWeek.THURSDAY,
      Amount.ZERO,
      DayOfWeek.FRIDAY,
      Amount.ZERO,
      DayOfWeek.SATURDAY,
      Amount.ZERO,
      DayOfWeek.SUNDAY,
      Amount.ZERO
    )
  );

  public WeeklyStatistics apply(GiftCardEvent event) {
    return switch (event) {
      case GifCardExhausted gifCardExhausted -> this;
      case GiftCardDeclared giftCardDeclared -> this;
      case PaidAmount paidAmount -> {
        var dayOfWeek = paidAmount.on().getDayOfWeek();
        yield add(dayOfWeek, paidAmount.amount());
      }
    };
  }

  private WeeklyStatistics add(DayOfWeek dayOfWeek, Amount amount) {
    var newValues = new HashMap<>(this.values);
    newValues.computeIfPresent(dayOfWeek, (__, currentAmount) -> currentAmount.plus(amount));

    return new WeeklyStatistics(newValues);
  }
}
