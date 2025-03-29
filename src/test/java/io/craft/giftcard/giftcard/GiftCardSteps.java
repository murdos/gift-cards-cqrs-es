package io.craft.giftcard.giftcard;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.craft.giftcard.giftcard.application.GiftCardApplicationService;
import io.craft.giftcard.giftcard.domain.Amount;
import io.craft.giftcard.giftcard.domain.Barcode;
import io.craft.giftcard.giftcard.domain.BarcodeAlreadyUsedException;
import io.craft.giftcard.giftcard.domain.ShoppingStore;
import io.craft.giftcard.giftcard.domain.commands.GiftCardDeclaration;
import io.craft.giftcard.giftcard.domain.commands.Payment;
import io.craft.giftcard.giftcard.infrastructure.secondary.InMemoryGiftCardCurrentStateRepository;
import io.craft.giftcard.giftcard.infrastructure.secondary.InMemoryGiftCardEventStore;
import io.craft.giftcard.giftcard.infrastructure.secondary.KafkaGiftCardMessageSender;
import io.craft.giftcard.giftcard.infrastructure.secondary.SimpleEventPublisher;
import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

public class GiftCardSteps {

  private final GiftCardApplicationService giftCardApplicationService =
    new GiftCardApplicationService(
      new InMemoryGiftCardEventStore(),
      new InMemoryGiftCardCurrentStateRepository(),
      new SimpleEventPublisher(),
      new KafkaGiftCardMessageSender(new ObjectMapper())
    );

  private Optional<Exception> declarationException;

  @ParameterType("\\d{4}-\\d{2}-\\d{2}")
  public LocalDate localDate(String date) {
    return LocalDate.parse(date);
  }

  @Before
  public void setUp() {
    declarationException = Optional.empty();
  }

  @When("I declare a new gift card")
  public void declareANewGiftCard(Map<String, String> giftCardInfos) {
    var giftCardDeclaration = new GiftCardDeclaration(
      new Barcode(giftCardInfos.get("barcode")),
      new Amount(new BigDecimal(giftCardInfos.get("amount"))),
      ShoppingStore.RESTAURANT_PANORAMIX
    );

    try {
      giftCardApplicationService.declare(giftCardDeclaration);
    } catch (Exception exception) {
      declarationException = Optional.of(exception);
    }
  }

  @Then("I should have an error because the gift card already exists")
  public void shouldHaveAnErrorBecauseTheGiftCardAlreadyExists() {
    assertThat(declarationException).hasValueSatisfying(exception -> {
      assertThat(exception).isInstanceOf(BarcodeAlreadyUsedException.class);
    });
  }

  @Then("the gift card {string} should have a remaining amount of {double}")
  public void giftCardShouldHaveARemainingAmountOf(String barcode, double expectedAmount) {
    var giftCardView = giftCardApplicationService.findBy(new Barcode(barcode));

    assertThat(giftCardView.remainingAmount().value()).isEqualByComparingTo(
      new BigDecimal(expectedAmount)
    );
  }

  @When("I pay with the gift card {string} an amount of {double} on {localDate}")
  public void iPayWithTheGiftCardAnAmountOf(
    String barcode,
    double paidAmount,
    LocalDate paymentDate
  ) {
    giftCardApplicationService.pay(
      new Barcode(barcode),
      new Payment(Amount.of(paidAmount), paymentDate)
    );
  }
}
