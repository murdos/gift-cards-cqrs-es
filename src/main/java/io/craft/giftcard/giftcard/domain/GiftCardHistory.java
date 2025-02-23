package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.giftcard.domain.events.GiftCardCreated;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import java.util.List;

public record GiftCardHistory(GiftCardCreated start, List<GiftCardEvent> followingEvents) {}
