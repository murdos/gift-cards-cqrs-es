package io.craft.giftcard.wire.jackson.infrastructure.primary;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.craft.giftcard.IntegrationTest;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class JacksonConfigurationIT {

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void shouldHandleOptional() throws JsonProcessingException {
    Optional<String> optional = Optional.of("test");
    assertThat(objectMapper.writeValueAsString(optional)).isEqualTo("\"test\"");
  }
}
