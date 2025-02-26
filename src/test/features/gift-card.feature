Feature: Use gift card

  Scenario: Declare a new gift card
    When I declare a new gift card
      | barcode | 1234567890 |
      | amount  | 100        |
    Then the gift card "1234567890" should have a remaining amount of 100.0

  Scenario: Pay with a gift card
    Given I declare a new gift card
      | barcode | 1234567890 |
      | amount  | 100        |
    When I pay with the gift card "1234567890" an amount of 40.0
    Then the gift card "1234567890" should have a remaining amount of 60.0

  Scenario: Pay with a gift card twice
    Given I declare a new gift card
      | barcode | 1234567890 |
      | amount  | 100        |
    And I pay with the gift card "1234567890" an amount of 40.0
    When I pay with the gift card "1234567890" an amount of 25.0
    Then the gift card "1234567890" should have a remaining amount of 35.0
