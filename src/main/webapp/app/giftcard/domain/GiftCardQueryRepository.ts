import type { GiftCard } from './GiftCard.ts';

export interface GiftCardQueryRepository {
  findAll(): Promise<GiftCard[]>;
}
