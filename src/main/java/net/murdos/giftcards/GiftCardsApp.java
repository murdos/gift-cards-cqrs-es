package net.murdos.giftcards;

import net.murdos.giftcards.shared.generation.domain.ExcludeFromGeneratedCodeCoverage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
@ExcludeFromGeneratedCodeCoverage(reason = "Not testing logs")
public class GiftCardsApp {

  private static final Logger log = LoggerFactory.getLogger(GiftCardsApp.class);

  public static void main(String[] args) {
    Environment env = SpringApplication.run(GiftCardsApp.class, args).getEnvironment();

    if (log.isInfoEnabled()) {
      log.info(ApplicationStartupTraces.of(env));
    }
  }
}
