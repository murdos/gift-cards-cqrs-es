import type { GiftCard } from '@/giftcard/domain/GiftCard.ts';
import type { GiftCardRepository } from '@/giftcard/domain/GiftCardRepository.ts';
import type { AxiosHttp } from '@/shared/http/infrastructure/secondary/AxiosHttp.ts';

export class AxiosGiftCardRepository implements GiftCardRepository {
  constructor(private readonly axiosHttp: AxiosHttp) {}

  async findAll(): Promise<GiftCard[]> {
    const response = await this.axiosHttp.get<GiftCard[]>('/api/gift-cards');
    return response.data;
  }
}
