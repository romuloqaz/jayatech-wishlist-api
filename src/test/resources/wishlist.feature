Feature: Wishlist

  Scenario: User creates a wishlist
    Given a user with userId "user123"
    When the user creates a wishlist
    Then a wishlist is created successfully

  Scenario: User not create
    Given a user with userId "user123"
    When the user doesn't create a wishlist
    Then should not create a wishlist a "400"

  Scenario: User get wishlist
    Given there is an existing wishlist by userId "user123"
    When I request the wishlist with ID
    Then the response with a wishlist by id

  Scenario: User get wishlist not found
    Given there is an existing wishlist with ID "not_found"
    When I request the wishlist with ID
    Then the response with a wishlist by id not found

  Scenario: Increment wishlist wish a product
    Given send a product id with id "1"
    Given there is an existing wishlist by userId "user123"
    Then increment wishlist with a product
    Then the response the wishlist with the product

  Scenario: Increment wishlist wish a product inserted before
    Given send a product id with id "1"
    Given there is an existing wishlist by userId "user123"
    Then increment wishlist with a product
    Then the response the wishlist exception product registered

  Scenario: Check wishlist with product id
    Given send a product id with id "1"
    Given there is an existing wishlist by userId "user123"
    When I request the wishlist with ID
    Then check if the product is on the wishlist
    Then the response should be checkProduct

  Scenario: Check wishlist with product non existing
    Given send a product id with id "non_existing"
    Given there is an existing wishlist by userId "user123"
    When I request the wishlist with ID
    Then check if the product is on the wishlist
    Then the response should be checkProduct not exists

  Scenario: Delete wishlistItem of a Wishlist by itemId
    Given there is an existing wishlist by userId "user123"
    When I request the wishlist with ID
    When get wishlist item
    Then delete a wishlistItem of a wishlist

  Scenario: Delete wishlistItem of a Wishlist by non existing itemId
    Given there is an existing wishlist by userId "user123"
    Given get wishlist item with itemId "non_existing"
    When I request the wishlist with ID
    Then delete a wishlistItem of a wishlist
    Then the exception should be wishlist item not found

  Scenario: CleanUpTests
    Then cleanUp by userId "user123"

