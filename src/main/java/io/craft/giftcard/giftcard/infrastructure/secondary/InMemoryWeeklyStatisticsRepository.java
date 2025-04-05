package io.craft.giftcard.giftcard.infrastructure.secondary;

import io.craft.giftcard.giftcard.domain.projections.WeeklyStatistics;
import io.craft.giftcard.giftcard.domain.projections.WeeklyStatisticsRepository;
import org.springframework.stereotype.Component;

@Component
public class InMemoryWeeklyStatisticsRepository implements WeeklyStatisticsRepository {

  private WeeklyStatistics weeklyStatistics = WeeklyStatistics.EMPTY;

  @Override
  public void save(WeeklyStatistics weeklyStatistics) {
    this.weeklyStatistics = weeklyStatistics;
  }

  @Override
  public WeeklyStatistics get() {
    return weeklyStatistics;
  }
}
