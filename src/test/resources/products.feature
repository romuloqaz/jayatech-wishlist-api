Feature: Product Operations

  Scenario: Retrieve an existing product by ID
    Given there is an existing product with ID "1"
    When I request the product with ID "1"
    Then the response status code should be 200
    And the response should contain the following product details:
      | Id           | 1             |
      | Name         | productTest5  |
      | Price        | 5             |
      | Description  | test product4 |

  Scenario: Retrieve a non-existing product by ID
    Given there is no product with ID "non_existing_id"
    When I request the product with ID "non_existing_id"
    Then I should receive a "404"