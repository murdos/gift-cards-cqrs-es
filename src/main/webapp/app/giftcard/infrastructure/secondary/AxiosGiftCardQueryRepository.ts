import type { GiftCard } from '@/giftcard/domain/GiftCard.ts';
import type { GiftCardDetails } from '@/giftcard/domain/GiftCardDetails.ts';
import type { GiftCardQueryRepository } from '@/giftcard/domain/GiftCardQueryRepository.ts';
import type { AxiosHttp } from '@/shared/http/infrastructure/secondary/AxiosHttp.ts';

export class AxiosGiftCardQueryRepository implements GiftCardQueryRepository {
  constructor(private readonly axiosHttp: AxiosHttp) {}

  async findAll(): Promise<GiftCard[]> {
    const response = await this.axiosHttp.get<GiftCard[]>('/api/gift-cards');
    return response.data;
  }

  async findDetailsBy(barcode: string): Promise<{ value: string[] }> {
    const response = await this.axiosHttp.get<GiftCardDetails>(`/api/gift-cards/${barcode}/details`);
    return { value: response.data.details };
  }
}
