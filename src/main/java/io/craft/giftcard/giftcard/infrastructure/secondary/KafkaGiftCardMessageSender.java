package io.craft.giftcard.giftcard.infrastructure.secondary;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.craft.giftcard.giftcard.domain.events.GiftCardDeclared;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import io.craft.giftcard.giftcard.domain.events.GiftCardExhausted;
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
    JSonGiftCardEvent jsonEvent =
      switch (event) {
        case GiftCardDeclared GiftCardDeclared -> JSonGiftCardDeclared.from(GiftCardDeclared);
        case GiftCardExhausted giftCardExhausted -> JSonGiftCardExhausted.from(giftCardExhausted);
        case PaidAmount paidAmount -> JSonPaidAmount.from(paidAmount);
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
  sealed interface JSonGiftCardEvent
    permits JSonGiftCardDeclared, JSonPaidAmount, JSonGiftCardExhausted {}

  record JSonGiftCardDeclared(String barcode, double amount) implements JSonGiftCardEvent {
    public static JSonGiftCardEvent from(GiftCardDeclared giftCardDeclared) {
      return new JSonGiftCardDeclared(
        giftCardDeclared.barcode().value(),
        giftCardDeclared.amount().value().doubleValue()
      );
    }
  }

  record JSonPaidAmount(String barcode, double amount) implements JSonGiftCardEvent {
    public static JSonGiftCardEvent from(PaidAmount paidAmount) {
      return new JSonPaidAmount(
        paidAmount.barcode().value(),
        paidAmount.amount().value().doubleValue()
      );
    }
  }

  record JSonGiftCardExhausted(String barcode) implements JSonGiftCardEvent {
    public static JSonGiftCardEvent from(GiftCardExhausted giftCardExhausted) {
      return new JSonGiftCardExhausted(giftCardExhausted.barcode().value());
    }
  }
}
