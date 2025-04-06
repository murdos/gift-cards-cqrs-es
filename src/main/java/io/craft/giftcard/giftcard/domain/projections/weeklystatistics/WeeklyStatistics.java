package io.craft.giftcard.giftcard.domain.projections.weeklystatistics;

import io.craft.giftcard.giftcard.domain.Amount;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;
import org.jmolecules.architecture.cqrs.QueryModel;

@QueryModel
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

  private WeeklyStatistics withAdditionalAmountToDayOfWeek(Amount amount, DayOfWeek dayOfWeek) {
    var newValues = new HashMap<>(this.values);
    newValues.computeIfPresent(dayOfWeek, (__, currentAmount) -> currentAmount.plus(amount));

    return new WeeklyStatistics(newValues);
  }
}
