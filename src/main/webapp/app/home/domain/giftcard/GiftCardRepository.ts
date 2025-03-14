import type { GiftCard } from '@/home/domain/giftcard/GiftCard.ts';

export interface GiftCardRepository {
  findAll(): Promise<GiftCard[]>;
}
