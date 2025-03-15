import type { GiftCardCommandRepository } from '@/giftcard/domain/GiftCardCommandRepository.ts';
import type { GiftCardDeclaration } from '@/giftcard/domain/GiftCardDeclaration.ts';
import type { AxiosHttp } from '@/shared/http/infrastructure/secondary/AxiosHttp.ts';

export class AxiosGiftCardCommandRepository implements GiftCardCommandRepository {
  constructor(private readonly axiosHttp: AxiosHttp) {}

  async declare(giftCardDeclaration: GiftCardDeclaration): Promise<void> {
    await this.axiosHttp.post('/api/gift-cards', {
      barcode: giftCardDeclaration.barcode.value,
      amount: giftCardDeclaration.amount.value,
    });
  }
}
