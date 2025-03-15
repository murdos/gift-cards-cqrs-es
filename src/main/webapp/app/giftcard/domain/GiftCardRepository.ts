import type { GiftCard } from './GiftCard.ts';

export interface GiftCardRepository {
  findAll(): Promise<GiftCard[]>;
}
