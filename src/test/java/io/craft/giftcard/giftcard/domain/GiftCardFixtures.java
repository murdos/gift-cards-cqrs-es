package io.craft.giftcard.giftcard.domain;

import java.time.LocalDate;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

public class GiftCardFixtures {

  public static Barcode barcode() {
    return new Barcode(RandomStringUtils.secure().nextNumeric(12));
  }

  public static Amount amount() {
    return Amount.of(RandomUtils.secure().randomDouble(0, 1000));
  }

  public static LocalDate paymentDate() {
    return LocalDate.now().minusDays(RandomUtils.secure().randomInt(1, 10));
  }
}
