import type { GiftCardDeclaration } from './GiftCardDeclaration.ts';

export interface GiftCardCommandRepository {
  declare(giftCardDeclaration: GiftCardDeclaration): void;
}
