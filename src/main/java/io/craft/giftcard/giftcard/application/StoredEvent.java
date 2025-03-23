package io.craft.giftcard.giftcard.application;

import io.craft.giftcard.giftcard.domain.SequenceId;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;

public record StoredEvent<T extends GiftCardEvent>(T event, SequenceId sequenceId) {}
