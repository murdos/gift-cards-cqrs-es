package io.craft.giftcard.shared.collection.domain;

import java.util.function.BinaryOperator;

public class DummyCombiner<T> implements BinaryOperator<T> {

  @Override
  public T apply(T combinable1, T combinable2) {
    return combinable1;
  }
}
