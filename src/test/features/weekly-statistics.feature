Feature: Weekly statistics

  Scenario: Pay with a gift card twice
    Given I declare a new gift card
      | barcode | 1234567890 |
      | amount  | 100        |
    When I pay with the gift card "1234567890" an amount of 40.0 on 2025-04-07
    And I pay with the gift card "1234567890" an amount of 25.0 on 2025-04-11
    And I pay with the gift card "1234567890" an amount of 25.0 on 2025-04-14
    Then the weekly statistics should be
      | Monday | 65.0 |
      | Friday | 25.0 |
