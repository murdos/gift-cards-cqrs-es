package io.craft.giftcard.giftcard.infrastructure.secondary;

import io.craft.giftcard.giftcard.domain.Barcode;
import io.craft.giftcard.giftcard.domain.projections.details.GiftCardDetails;
import io.craft.giftcard.giftcard.domain.projections.details.GiftCardDetailsRepository;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.jmolecules.architecture.hexagonal.Adapter;
import org.springframework.stereotype.Component;

@Adapter
@Component
public class InMemoryGiftCardDetailsRepository implements GiftCardDetailsRepository {

  private final ConcurrentMap<Barcode, GiftCardDetails> store = new ConcurrentHashMap<>();

  @Override
  public void save(GiftCardDetails view) {
    store.put(view.barcode(), view);
  }

  @Override
  public Optional<GiftCardDetails> get(Barcode barcode) {
    return Optional.ofNullable(store.get(barcode));
  }

  @Override
  public List<GiftCardDetails> findAll() {
    return List.copyOf(store.values());
  }
}
