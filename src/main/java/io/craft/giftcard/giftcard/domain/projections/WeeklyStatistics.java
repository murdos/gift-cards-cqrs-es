package io.craft.giftcard.giftcard.domain.projections;

import io.craft.giftcard.giftcard.domain.Amount;
import java.time.DayOfWeek;
import java.util.Map;

public record WeeklyStatistics(Map<DayOfWeek, Amount> values) {
  // @formatter:off
  public static final WeeklyStatistics EMPTY = new WeeklyStatistics(
    Map.of(
      DayOfWeek.MONDAY, Amount.ZERO,
      DayOfWeek.TUESDAY, Amount.ZERO,
      DayOfWeek.WEDNESDAY, Amount.ZERO,
      DayOfWeek.THURSDAY, Amount.ZERO,
      DayOfWeek.FRIDAY, Amount.ZERO,
      DayOfWeek.SATURDAY, Amount.ZERO,
      DayOfWeek.SUNDAY, Amount.ZERO
    )
  );
  // @formatter:on

  public Amount get(DayOfWeek dayOfWeek) {
    return values.get(dayOfWeek);
  }
}
