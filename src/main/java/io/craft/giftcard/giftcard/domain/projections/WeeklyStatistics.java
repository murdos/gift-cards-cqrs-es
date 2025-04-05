package io.craft.giftcard.giftcard.domain.projections;

import io.craft.giftcard.giftcard.domain.Amount;
import java.time.DayOfWeek;
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
}
