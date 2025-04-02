import type { WeeklyStatistics } from '@/giftcard/domain/WeeklyStatistics.ts';
import type { GiftCard } from './GiftCard.ts';

export interface GiftCardQueryRepository {
  findAll(): Promise<GiftCard[]>;

  getWeeklyStatistics(): Promise<WeeklyStatistics | null>;
}
