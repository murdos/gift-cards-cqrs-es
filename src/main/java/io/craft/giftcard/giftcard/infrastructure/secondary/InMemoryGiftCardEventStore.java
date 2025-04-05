package io.craft.giftcard.giftcard.infrastructure.secondary;

import io.craft.giftcard.giftcard.domain.Barcode;
import io.craft.giftcard.giftcard.domain.GiftCardEventStore;
import io.craft.giftcard.giftcard.domain.SequenceId;
import io.craft.giftcard.giftcard.domain.events.GiftCardDeclared;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import io.craft.giftcard.giftcard.domain.events.GiftCardHistory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.jmolecules.architecture.hexagonal.Adapter;
import org.springframework.stereotype.Component;

@Adapter
@Component
public class InMemoryGiftCardEventStore implements GiftCardEventStore, InMemorySecondaryAdapter {

  private final Map<Barcode, List<GiftCardEvent>> histories = new HashMap<>();
  private final Map<Barcode, List<SequenceId>> existingSequenceIds = new HashMap<>();

  @Override
  public void save(GiftCardEvent event) {
    histories.computeIfAbsent(event.barcode(), key -> new ArrayList<>()).add(event);

    List<SequenceId> sequenceIds = existingSequenceIds.computeIfAbsent(event.barcode(), key ->
      new ArrayList<>()
    );
    if (sequenceIds.contains(event.sequenceId())) {
      throw new IllegalArgumentException("SequenceId already exists");
    }
    sequenceIds.add(event.sequenceId());
  }

  @Override
  public void save(List<GiftCardEvent> events) {
    events.forEach(this::save);
  }

  @Override
  public GiftCardHistory getHistory(Barcode barcode) {
    var events = histories.get(barcode);

    var followingEvents = events.stream().skip(1).toList();

    return new GiftCardHistory((GiftCardDeclared) events.getFirst(), followingEvents);
  }

  @Override
  public Stream<GiftCardEvent> stream() {
    return histories.values().stream().flatMap(List::stream);
  }

  @Override
  public void reset() {
    histories.clear();
    existingSequenceIds.clear();
  }
}
