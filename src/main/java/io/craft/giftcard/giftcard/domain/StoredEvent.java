package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;

public record StoredEvent<T extends GiftCardEvent>(T event, SequenceId sequenceId) {}
