package io.craft.giftcard.giftcard.infrastructure.primary;

import io.craft.giftcard.giftcard.domain.Barcode;
import io.craft.giftcard.giftcard.domain.GiftCardView;
import io.craft.giftcard.giftcard.domain.GiftCardViewRepository;
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

  private final GiftCardViewRepository giftCardViewRepository;

  public GiftCardQueryController(GiftCardViewRepository giftCardViewRepository) {
    this.giftCardViewRepository = giftCardViewRepository;
  }

  @GetMapping
  public Collection<GiftCardView> getAllGiftCards() {
    return giftCardViewRepository.findAll();
  }

  @GetMapping("/{barcode}")
  public ResponseEntity<GiftCardView> getGiftCardByBarcode(@PathVariable String barcode) {
    return giftCardViewRepository.get(new Barcode(barcode)).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }
}
