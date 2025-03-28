Feature: Use gift card

  Scenario: Declare a new gift card
    When I declare a new gift card
      | barcode | 1234567890 |
      | amount  | 100        |
    Then the gift card "1234567890" should have a remaining amount of 100.0

  Scenario: Declare an existing gift card
    Given I declare a new gift card
      | barcode | 1234567890 |
      | amount  | 100        |
    Given I declare a new gift card
      | barcode | 1234567890 |
      | amount  | 50         |
    Then I should have an error because the gift card already exists
    Then the gift card "1234567890" should have a remaining amount of 100.0

  Scenario: Pay with a gift card
    Given I declare a new gift card
      | barcode | 1234567890 |
      | amount  | 100        |
    When I pay with the gift card "1234567890" an amount of 40.0 on 2025-01-10
    Then the gift card "1234567890" should have a remaining amount of 60.0

  Scenario: Pay with a gift card twice
    Given I declare a new gift card
      | barcode | 1234567890 |
      | amount  | 100        |
    And I pay with the gift card "1234567890" an amount of 40.0 on 2025-01-10
    When I pay with the gift card "1234567890" an amount of 25.0 on 2025-02-12
    Then the gift card "1234567890" should have a remaining amount of 35.0
