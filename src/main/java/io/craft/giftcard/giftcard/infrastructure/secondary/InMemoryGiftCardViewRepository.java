package io.craft.giftcard.giftcard.infrastructure.secondary;

import io.craft.giftcard.giftcard.domain.Barcode;
import io.craft.giftcard.giftcard.domain.GiftCardView;
import io.craft.giftcard.giftcard.domain.GiftCardViewRepository;
import java.util.*;
import org.jmolecules.architecture.hexagonal.Adapter;
import org.springframework.stereotype.Component;

@Adapter
@Component
public class InMemoryGiftCardViewRepository implements GiftCardViewRepository {

  private final Map<Barcode, GiftCardView> views = new HashMap<>();

  @Override
  public void save(GiftCardView view) {
    views.put(view.barcode(), view);
  }

  @Override
  public Optional<GiftCardView> get(Barcode barcode) {
    return Optional.ofNullable(views.get(barcode));
  }

  @Override
  public List<GiftCardView> findAll() {
    return views.values().stream().toList();
  }
}
