import type { GiftCard } from '@/home/domain/giftcard/GiftCard.ts';
import type { GiftCardRepository } from '@/home/domain/giftcard/GiftCardRepository.ts';

export class GiftCardService {
  constructor(private readonly giftCardRepository: GiftCardRepository) {}

  async getGiftCards(): Promise<GiftCard[]> {
    return this.giftCardRepository.findAll();
  }
}
