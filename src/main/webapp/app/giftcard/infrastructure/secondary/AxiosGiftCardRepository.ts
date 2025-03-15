import type { GiftCard } from '@/home/domain/giftcard/GiftCard.ts';
import type { GiftCardRepository } from '@/home/domain/giftcard/GiftCardRepository.ts';
import type { AxiosHttp } from '@/shared/http/infrastructure/secondary/AxiosHttp.ts';

export class AxiosGiftCardRepository implements GiftCardRepository {
  constructor(private readonly axiosHttp: AxiosHttp) {}

  async findAll(): Promise<GiftCard[]> {
    const response = await this.axiosHttp.get<GiftCard[]>('/api/gift-cards');
    return response.data;
  }
}
