package io.craft.giftcard.giftcard.domain.projections.currentstate;

import io.craft.giftcard.giftcard.domain.Barcode;
import java.util.List;
import java.util.Optional;
import org.jmolecules.architecture.hexagonal.Port;

@Port
public interface GiftCardCurrentStateRepository {
  void save(GiftCardCurrentState view);

  Optional<GiftCardCurrentState> get(Barcode barcode);

  List<GiftCardCurrentState> findAll();
}
