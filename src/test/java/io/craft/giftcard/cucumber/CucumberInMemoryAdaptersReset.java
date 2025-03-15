package io.craft.giftcard.cucumber;

import io.craft.giftcard.giftcard.infrastructure.secondary.InMemorySecondaryAdapter;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;

public class CucumberInMemoryAdaptersReset {

  private final Collection<InMemorySecondaryAdapter> repositories = new ArrayList<>();

  CucumberInMemoryAdaptersReset(@Autowired(required = false) Collection<InMemorySecondaryAdapter> repositories) {
    if (repositories != null) {
      this.repositories.addAll(repositories);
    }
  }

  @After
  @Before
  public void wipeData() {
    repositories.forEach(InMemorySecondaryAdapter::reset);
  }
}
