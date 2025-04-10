package io.craft.giftcard.giftcard.infrastructure.primary;

import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;

import io.craft.giftcard.giftcard.application.GiftCardApplicationService;
import io.craft.giftcard.giftcard.domain.Amount;
import io.craft.giftcard.giftcard.domain.Barcode;
import io.craft.giftcard.giftcard.domain.projections.currentstate.GiftCardCurrentState;
import io.craft.giftcard.giftcard.domain.projections.details.GiftCardDetails;
import io.craft.giftcard.giftcard.domain.projections.weeklystatistics.WeeklyStatistics;
import jakarta.annotation.Nullable;
import java.time.DayOfWeek;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/gift-cards")
class GiftCardQueryController {

  private final GiftCardApplicationService giftCards;

  GiftCardQueryController(GiftCardApplicationService giftCards) {
    this.giftCards = giftCards;
  }

  @GetMapping
  public Collection<GiftCardCurrentStateDto> getAllGiftCards() {
    return giftCards
      .findAllCurrentStates()
      .stream()
      .map(GiftCardCurrentStateDto::fromDomain)
      .toList();
  }

  @GetMapping("/{barcode}")
  public ResponseEntity<GiftCardCurrentStateDto> getGiftCardByBarcode(
    @PathVariable String barcode
  ) {
    return giftCards
      .getCurrentState(new Barcode(barcode))
      .map(GiftCardCurrentStateDto::fromDomain)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/{barcode}/details")
  public ResponseEntity<GiftCardDetails> getDetails(@PathVariable String barcode) {
    return giftCards
      .getDetails(new Barcode(barcode))
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  public record GiftCardCurrentStateDto(
    Barcode barcode,
    Amount remainingAmount,
    ShoppingStoreDto shoppingStore,
    boolean exhausted
  ) {
    public static GiftCardCurrentStateDto fromDomain(GiftCardCurrentState giftCardCurrentState) {
      return new GiftCardCurrentStateDto(
        giftCardCurrentState.barcode(),
        giftCardCurrentState.remainingAmount(),
        new ShoppingStoreDto(
          QueryShoppingStore.valueOf(giftCardCurrentState.shoppingStore().name())
        ),
        giftCardCurrentState.exhausted()
      );
    }
  }

  @Nullable
  @GetMapping("/statistics")
  public WeeklyStatisticsDto getWeeklyStatistics() {
    // WeeklyStatisticsDto.fromDomain()
    return null;
  }
}

record WeeklyStatisticsDto(Map<DayOfWeek, Double> values) {
  public static WeeklyStatisticsDto fromDomain(WeeklyStatistics weeklyStatistics) {
    return new WeeklyStatisticsDto(
      weeklyStatistics
        .values()
        .entrySet()
        .stream()
        .collect(
          Collectors.toMap(Map.Entry::getKey, dayOfWeekAmountEntry ->
            dayOfWeekAmountEntry.getValue().value().doubleValue()
          )
        )
    );
  }
}

record ShoppingStoreDto(QueryShoppingStore value) {}

enum QueryShoppingStore {
  POISSONNERIE,
  FORGE,
  RESTAURANT,
  LIVREUR,
  MUSIQUE,
}
