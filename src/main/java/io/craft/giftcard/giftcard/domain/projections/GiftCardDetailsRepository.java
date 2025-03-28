package io.craft.giftcard.giftcard.domain.projections;

import io.craft.giftcard.giftcard.domain.Barcode;
import java.util.List;
import java.util.Optional;
import org.jmolecules.architecture.hexagonal.Port;

@Port
public interface GiftCardDetailsRepository {
  void save(GiftCardDetails view);

  Optional<GiftCardDetails> get(Barcode barcode);

  List<GiftCardDetails> findAll();
}
