package net.murdos.giftcards.wire.liquibase.infrastructure.secondary;

import static org.assertj.core.api.Assertions.assertThat;

import liquibase.integration.spring.SpringLiquibase;
import net.murdos.giftcards.IntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

@IntegrationTest
class LiquibaseConfigurationIT {

  @Nested
  @IntegrationTest(properties = { "application.liquibase.async=true" })
  class Async {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    void shouldGetLiquibaseAsync() {
      SpringLiquibase springLiquibase = (SpringLiquibase) applicationContext.getBean("liquibase");

      assertThat(springLiquibase).isNotNull();
    }
  }

  @Nested
  @IntegrationTest(properties = { "application.liquibase.async=false" })
  class Sync {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    void shouldGetLiquibaseSync() {
      SpringLiquibase springLiquibase = (SpringLiquibase) applicationContext.getBean("liquibase");

      assertThat(springLiquibase).isNotNull();
    }
  }
}
