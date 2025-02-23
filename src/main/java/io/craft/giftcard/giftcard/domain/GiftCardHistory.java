package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.giftcard.domain.events.GiftCardCreated;

public record GiftCardHistory(GiftCardCreated start) {}
