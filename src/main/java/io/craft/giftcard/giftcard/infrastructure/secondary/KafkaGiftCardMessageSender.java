package io.craft.giftcard.giftcard.infrastructure.secondary;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.craft.giftcard.giftcard.domain.events.CardReloaded;
import io.craft.giftcard.giftcard.domain.events.GifCardExhausted;
import io.craft.giftcard.giftcard.domain.events.GiftCardCreated;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import io.craft.giftcard.giftcard.domain.events.PaidAmount;
import io.craft.giftcard.giftcard.domain.projections.GiftCardMessageSender;
import java.io.UncheckedIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Fake event handler that is supposed to send events serialized in JSON to Kafka.
 */
@Component
public class KafkaGiftCardMessageSender implements GiftCardMessageSender {

  private static final Logger logs = LoggerFactory.getLogger(KafkaGiftCardMessageSender.class);

  // Here you should have a KafkaTemplate field
  // ...

  private final ObjectMapper jsonMapper;

  public KafkaGiftCardMessageSender(ObjectMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  @Override
  public void send(GiftCardEvent event) {
    if (event instanceof CardReloaded) {
      return;
    }
    JSonGiftCardEvent jsonEvent =
      switch (event) {
        case GiftCardCreated giftCardCreated -> JSonGiftCardCreated.from(giftCardCreated);
        case GifCardExhausted gifCardExhausted -> JSonGifCardExhausted.from(gifCardExhausted);
        case PaidAmount paidAmount -> JSonPaidAmount.from(paidAmount);
        case CardReloaded cardReloaded -> throw new IllegalArgumentException("CardReloaded event should not be sent to Kafka");
      };

    try {
      String json = jsonMapper.writeValueAsString(jsonEvent);
      // publish json to kafka using the kafkaTemplate
      logs.info("Event sent to kafka: {}", json);
    } catch (JsonProcessingException exception) {
      throw new UncheckedIOException(exception);
    }
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.SIMPLE_NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
  sealed interface JSonGiftCardEvent permits JSonGiftCardCreated, JSonPaidAmount, JSonGifCardExhausted {}

  record JSonGiftCardCreated(String barcode, double amount) implements JSonGiftCardEvent {
    public static JSonGiftCardEvent from(GiftCardCreated giftCardCreated) {
      return new JSonGiftCardCreated(giftCardCreated.barcode().value(), giftCardCreated.amount().value().doubleValue());
    }
  }

  record JSonPaidAmount(String barcode, double amount) implements JSonGiftCardEvent {
    public static JSonGiftCardEvent from(PaidAmount paidAmount) {
      return new JSonPaidAmount(paidAmount.barcode().value(), paidAmount.amount().value().doubleValue());
    }
  }

  record JSonGifCardExhausted(String barcode) implements JSonGiftCardEvent {
    public static JSonGiftCardEvent from(GifCardExhausted gifCardExhausted) {
      return new JSonGifCardExhausted(gifCardExhausted.barcode().value());
    }
  }
}
