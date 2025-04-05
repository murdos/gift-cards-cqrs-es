package io.craft.giftcard.giftcard.domain.projections;

import io.craft.giftcard.giftcard.domain.EventHandler;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;

public class WeeklyStatisticsUpdater implements EventHandler<GiftCardEvent> {

  private final WeeklyStatisticsRepository weeklyStatisticsRepository;

  public WeeklyStatisticsUpdater(WeeklyStatisticsRepository weeklyStatisticsRepository) {
    this.weeklyStatisticsRepository = weeklyStatisticsRepository;
  }

  @Override
  public void handle(GiftCardEvent event) {
    WeeklyStatistics weeklyStatistics = weeklyStatisticsRepository.get();
    weeklyStatisticsRepository.save(weeklyStatistics.apply(event));
  }
}
