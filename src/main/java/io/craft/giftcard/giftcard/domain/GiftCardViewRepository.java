package io.craft.giftcard.giftcard.domain;

import org.jmolecules.architecture.hexagonal.Port;

@Port
public interface GiftCardViewRepository {
  void save(GiftCardView view);

  GiftCardView get(Barcode barcode);
}
