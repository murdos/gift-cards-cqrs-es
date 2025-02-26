package io.craft.giftcard.giftcard.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

public class GiftCardFixtures {

  public static Barcode barcode() {
    return new Barcode(RandomStringUtils.secure().nextNumeric(12));
  }

  public static Amount amount() {
    return new Amount(BigDecimal.valueOf(RandomUtils.secure().randomDouble(0, 1000)));
  }
}
