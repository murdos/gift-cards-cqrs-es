package io.craft.giftcard.giftcard.domain;

import java.util.Optional;
import org.jmolecules.architecture.hexagonal.Port;

@Port
public interface GiftCardViewRepository {
  void save(GiftCardView view);

  Optional<GiftCardView> get(Barcode barcode);
}
