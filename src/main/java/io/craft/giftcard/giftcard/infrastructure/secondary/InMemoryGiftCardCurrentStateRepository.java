package io.craft.giftcard.giftcard.infrastructure.secondary;

import io.craft.giftcard.giftcard.domain.Barcode;
import io.craft.giftcard.giftcard.domain.projections.GiftCardCurrentState;
import io.craft.giftcard.giftcard.domain.projections.GiftCardCurrentStateRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.jmolecules.architecture.hexagonal.Adapter;
import org.springframework.stereotype.Component;

@Adapter
@Component
public class InMemoryGiftCardCurrentStateRepository
  implements GiftCardCurrentStateRepository, InMemorySecondaryAdapter {

  private final Map<Barcode, GiftCardCurrentState> views = new HashMap<>();

  @Override
  public void save(GiftCardCurrentState view) {
    views.put(view.barcode(), view);
  }

  @Override
  public Optional<GiftCardCurrentState> get(Barcode barcode) {
    return Optional.ofNullable(views.get(barcode));
  }

  @Override
  public List<GiftCardCurrentState> findAll() {
    return views.values().stream().toList();
  }

  @Override
  public void reset() {
    views.clear();
  }
}
