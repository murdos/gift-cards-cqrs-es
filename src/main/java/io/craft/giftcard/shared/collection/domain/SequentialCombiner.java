package io.craft.giftcard.shared.collection.domain;

import java.util.function.BinaryOperator;

public class SequentialCombiner<T> implements BinaryOperator<T> {

  @Override
  public T apply(T combinable1, T combinable2) {
    throw new IllegalStateException(
      "A projection can not be streamed in parallel and thus cannot be combined"
    );
  }
}
