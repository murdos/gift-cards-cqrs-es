package io.craft.giftcard.giftcard.infrastructure.primary;

import io.craft.giftcard.giftcard.domain.Amount;
import io.craft.giftcard.giftcard.domain.Barcode;
import io.craft.giftcard.giftcard.domain.projections.GiftCardCurrentState;
import io.craft.giftcard.giftcard.domain.projections.GiftCardCurrentStateRepository;
import java.util.Collection;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/gift-cards")
class GiftCardQueryController {

  private final GiftCardCurrentStateRepository giftCardCurrentStateRepository;

  public GiftCardQueryController(GiftCardCurrentStateRepository giftCardCurrentStateRepository) {
    this.giftCardCurrentStateRepository = giftCardCurrentStateRepository;
  }

  @GetMapping
  public Collection<GiftCardCurrentStateDto> getAllGiftCards() {
    return giftCardCurrentStateRepository
      .findAll()
      .stream()
      .map(giftCardCurrentState -> GiftCardCurrentStateDto.fromDomain(giftCardCurrentState))
      .toList();
  }

  @GetMapping("/{barcode}")
  public ResponseEntity<GiftCardCurrentStateDto> getGiftCardByBarcode(
    @PathVariable String barcode
  ) {
    return giftCardCurrentStateRepository
      .get(new Barcode(barcode))
      .map(GiftCardCurrentStateDto::fromDomain)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  public record GiftCardCurrentStateDto(
    Barcode barcode,
    Amount remainingAmount,
    ShoppingStoreDto shoppingStore
  ) {
    public static GiftCardCurrentStateDto fromDomain(GiftCardCurrentState giftCardCurrentState) {
      return new GiftCardCurrentStateDto(
        giftCardCurrentState.barcode(),
        giftCardCurrentState.remainingAmount(),
        new ShoppingStoreDto(
          QueryShoppingStore.valueOf(giftCardCurrentState.shoppingStore().name())
        )
      );
    }
  }

  public record ShoppingStoreDto(QueryShoppingStore value) {}

  public enum QueryShoppingStore {
    POISSONNERIE_ORDRALPHABETIX,
    FORGE_CETAUTOMATIX,
    RESTAURANT_PANORAMIX,
  }
}
