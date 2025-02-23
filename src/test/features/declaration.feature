Feature: Declare a new gift card

  Scenario: Declare a new gift card
    When I declare a new gift card
      | barcode | 1234567890 |
      | amount  | 100        |
    Then the gift card "1234567890" should have a remaining amount of 100.0
