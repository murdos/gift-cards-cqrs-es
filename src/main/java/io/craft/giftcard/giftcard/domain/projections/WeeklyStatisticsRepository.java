package io.craft.giftcard.giftcard.domain.projections;

import org.jmolecules.architecture.hexagonal.Port;

@Port
public interface WeeklyStatisticsRepository {
  void save(WeeklyStatistics weeklyStatistics);

  WeeklyStatistics get();
}
