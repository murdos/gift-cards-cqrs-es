package io.craft.giftcard.giftcard;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.craft.giftcard.giftcard.application.GiftCardApplicationService;
import io.craft.giftcard.giftcard.domain.Amount;
import io.craft.giftcard.giftcard.domain.Barcode;
import io.craft.giftcard.giftcard.domain.ShoppingStore;
import io.craft.giftcard.giftcard.domain.commands.GiftCardDeclaration;
import io.craft.giftcard.giftcard.domain.commands.Payment;
import io.craft.giftcard.giftcard.infrastructure.secondary.InMemoryGiftCardCurrentStateRepository;
import io.craft.giftcard.giftcard.infrastructure.secondary.InMemoryGiftCardEventStore;
import io.craft.giftcard.giftcard.infrastructure.secondary.KafkaGiftCardMessageSender;
import io.craft.giftcard.giftcard.infrastructure.secondary.SimpleEventPublisher;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.math.BigDecimal;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

public class GiftCardSteps {

  private final GiftCardApplicationService giftCardApplicationService = new GiftCardApplicationService(
    new InMemoryGiftCardEventStore(),
    new InMemoryGiftCardCurrentStateRepository(),
    new SimpleEventPublisher(),
    new KafkaGiftCardMessageSender(new ObjectMapper())
  );

  @When("I declare a new gift card")
  public void declareANewGiftCard(Map<String, String> giftCardInfos) {
    var giftCardDeclaration = new GiftCardDeclaration(
      new Barcode(giftCardInfos.get("barcode")),
      new Amount(new BigDecimal(giftCardInfos.get("amount"))),
      ShoppingStore.RESTAURANT_PANORAMIX
    );
    giftCardApplicationService.declare(giftCardDeclaration);
  }

  @Then("the gift card {string} should have a remaining amount of {double}")
  public void giftCardShouldHaveARemainingAmountOf(String barcode, double expectedAmount) {
    var giftCardView = giftCardApplicationService.findBy(new Barcode(barcode));

    assertThat(giftCardView.remainingAmount().value()).isEqualByComparingTo(new BigDecimal(expectedAmount));
  }

  @When("I pay with the gift card {string} an amount of {double}")
  public void iPayWithTheGiftCardAnAmountOf(String barcode, double paidAmount) {
    giftCardApplicationService.pay(new Barcode(barcode), new Payment(new Amount(BigDecimal.valueOf(paidAmount))));
  }
}
