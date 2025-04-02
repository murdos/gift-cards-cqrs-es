package io.craft.giftcard.giftcard.infrastructure.primary;

import io.craft.giftcard.giftcard.application.GiftCardApplicationService;
import io.craft.giftcard.giftcard.domain.Amount;
import io.craft.giftcard.giftcard.domain.Barcode;
import io.craft.giftcard.giftcard.domain.ShoppingStore;
import io.craft.giftcard.giftcard.domain.commands.GiftCardDeclaration;
import io.craft.giftcard.giftcard.domain.commands.Payment;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/gift-cards")
class GiftCardCommandController {

  private final GiftCardApplicationService giftCards;

  public GiftCardCommandController(GiftCardApplicationService giftCards) {
    this.giftCards = giftCards;
  }

  @PostMapping
  ResponseEntity<Void> declare(
    @Valid @NotNull @RequestBody GiftCardDeclarationDTO giftCardDeclarationDTO
  ) {
    giftCards.declare(giftCardDeclarationDTO.toDomain());
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/{barcode}/pay")
  public ResponseEntity<Void> pay(
    @PathVariable String barcode,
    @Valid @NotNull @RequestBody PaymentDTO paymentDTO
  ) {
    giftCards.pay(new Barcode(barcode), paymentDTO.toDomain());
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}

record GiftCardDeclarationDTO(
  @NotBlank String barcode,
  @NotNull @Positive Integer amount,
  @NotNull String shoppingStore
) {
  public GiftCardDeclaration toDomain() {
    return new GiftCardDeclaration(
      new Barcode(barcode),
      new Amount(BigDecimal.valueOf(amount)),
      ShoppingStore.valueOf(shoppingStore)
    );
  }
}

record PaymentDTO(@NotNull @Positive Double amount, LocalDate paymentDate) {
  public Payment toDomain() {
    return new Payment(Amount.of(amount), Optional.ofNullable(paymentDate).orElse(LocalDate.now()));
  }
}
