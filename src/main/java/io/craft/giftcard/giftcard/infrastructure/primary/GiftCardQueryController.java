package io.craft.giftcard.giftcard.infrastructure.primary;

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
  public Collection<GiftCardCurrentState> getAllGiftCards() {
    return giftCardCurrentStateRepository.findAll();
  }

  @GetMapping("/{barcode}")
  public ResponseEntity<GiftCardCurrentState> getGiftCardByBarcode(@PathVariable String barcode) {
    return giftCardCurrentStateRepository.get(new Barcode(barcode)).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }
}
