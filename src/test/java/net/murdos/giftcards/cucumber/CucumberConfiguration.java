package net.murdos.giftcards.cucumber;

import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import java.nio.charset.StandardCharsets;
import java.util.List;
import net.murdos.giftcards.GiftCardsApp;
import net.murdos.giftcards.cucumber.CucumberConfiguration.CucumberRestTemplateConfiguration;
import net.murdos.giftcards.cucumber.rest.CucumberRestTemplate;
import net.murdos.giftcards.cucumber.rest.CucumberRestTestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

@ActiveProfiles("test")
@CucumberContextConfiguration
@SpringBootTest(classes = { GiftCardsApp.class, CucumberRestTemplateConfiguration.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
public class CucumberConfiguration {

  @Autowired
  private TestRestTemplate rest;

  @Before
  public void resetTestContext() {
    CucumberRestTestContext.reset();
  }

  @Before
  public void loadInterceptors() {
    ClientHttpRequestFactory requestFactory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());

    RestTemplate template = rest.getRestTemplate();
    template.setRequestFactory(requestFactory);
    template.setInterceptors(List.of(saveLastResultInterceptor()));
    template.getMessageConverters().addFirst(new StringHttpMessageConverter(StandardCharsets.UTF_8));
  }

  private ClientHttpRequestInterceptor saveLastResultInterceptor() {
    return (request, body, execution) -> {
      ClientHttpResponse response = execution.execute(request, body);

      CucumberRestTestContext.addResponse(request, response, execution, body);

      return response;
    };
  }

  @TestConfiguration
  static class CucumberRestTemplateConfiguration {

    @Autowired
    private TestRestTemplate rest;

    @Bean
    CucumberRestTemplate cucumberRestTemplate() {
      return new CucumberRestTemplate(rest);
    }
  }
}
