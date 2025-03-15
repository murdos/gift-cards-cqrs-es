package io.craft.giftcard.giftcard.infrastructure.primary;

import io.craft.giftcard.giftcard.application.GiftCardApplicationService;
import io.craft.giftcard.giftcard.domain.Amount;
import io.craft.giftcard.giftcard.domain.Barcode;
import io.craft.giftcard.giftcard.domain.commands.GiftCardDeclaration;
import io.craft.giftcard.shared.error.domain.Assert;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gift-cards")
public class GiftCardCommandController {

  private final GiftCardApplicationService giftCardApplicationService;

  public GiftCardCommandController(GiftCardApplicationService giftCardApplicationService) {
    this.giftCardApplicationService = giftCardApplicationService;
  }

  @PostMapping
  public ResponseEntity<Void> declare(@Valid @RequestBody GiftCardDeclarationDTO giftCardDeclarationDTO) {
    Assert.notNull("giftCardDeclarationDTO", giftCardDeclarationDTO);
    GiftCardDeclaration command = new GiftCardDeclaration(
      new Barcode(giftCardDeclarationDTO.barcode()),
      new Amount(BigDecimal.valueOf(giftCardDeclarationDTO.amount()))
    );
    giftCardApplicationService.declare(command);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}

record GiftCardDeclarationDTO(@NotBlank String barcode, @NotNull @Positive Integer amount) {}
