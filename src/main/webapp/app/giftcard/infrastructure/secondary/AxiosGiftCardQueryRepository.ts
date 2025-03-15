import type { GiftCard } from '@/giftcard/domain/GiftCard.ts';
import type { GiftCardQueryRepository } from '@/giftcard/domain/GiftCardQueryRepository.ts';
import type { AxiosHttp } from '@/shared/http/infrastructure/secondary/AxiosHttp.ts';

export class AxiosGiftCardQueryRepository implements GiftCardQueryRepository {
  constructor(private readonly axiosHttp: AxiosHttp) {}

  async findAll(): Promise<GiftCard[]> {
    const response = await this.axiosHttp.get<GiftCard[]>('/api/gift-cards');
    return response.data;
  }
}
