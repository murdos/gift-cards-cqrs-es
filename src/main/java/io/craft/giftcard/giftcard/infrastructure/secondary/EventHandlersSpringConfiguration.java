package io.craft.giftcard.giftcard.infrastructure.secondary;

import io.craft.giftcard.giftcard.domain.EventHandler;
import io.craft.giftcard.giftcard.domain.GiftCard;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
  basePackageClasses = { GiftCard.class },
  includeFilters = { @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { EventHandler.class }) }
)
class EventHandlersSpringConfiguration {}
