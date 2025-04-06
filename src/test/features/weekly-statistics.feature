@disabled
Feature: Weekly statistics

  Scenario: Should update weekly statistics on payment
    Given I declare a new gift card
      | barcode | 1234567890 |
      | amount  | 100        |
    When I pay with the gift card "1234567890" an amount of 10.0 on 2025-04-06
    When I pay with the gift card "1234567890" an amount of 15.0 on 2025-04-07
    When I pay with the gift card "1234567890" an amount of 40.0 on 2025-04-14
    Then the weekly statistics should be
      | Sunday | 10.0 |
      | Monday | 55.0 |
