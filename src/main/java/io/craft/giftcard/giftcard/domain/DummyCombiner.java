package io.craft.giftcard.giftcard.domain;

import java.util.function.BinaryOperator;

class DummyCombiner<T> implements BinaryOperator<T> {

  @Override
  public T apply(T combinable1, T combinable2) {
    return combinable1;
  }
}
