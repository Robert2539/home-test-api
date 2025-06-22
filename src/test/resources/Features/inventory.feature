Feature: Inventory API

  Scenario: Get all menu items
    Given the API at api-inventory is up and running
    When I send a GET request to inventory api
    Then the response status should be 200
    And the response should contain at least 13 items
    And each item in the response should have id name price image

  Scenario: Filter by id
    Given the API at api-inventory is up and running
    When I send a GET request to filter API with id=3 in query parameter
    Then the response status should be 200
    And the response should contain the item with id "3", name "Baked Rolls x 8", price "$10", and image "roll.png"

  Scenario: Add item with non-existent id
    Given the API at api-inventory is up and running
    When a POST request is sent to add API with body id "14", name "Hawaiian", image "hawaiian.png", and price "$14"
    Then the response status should be 200

  Scenario: Add item with existent id
    Given the API at api-inventory is up and running
    When a POST request is sent to add API with body id "14", name "Hawaiian", image "hawaiian.png", and price "$14"
    Then the response status should be 400

  Scenario: Try to add item with missing information
    Given the API at api-inventory is up and running
    When a POST request is sent to add API with body name "Hawaiian", image "hawaiian.png", and price "$14"
    Then the response status should be 400
    And the response body should contain the message "Not all requirements are met"
    
Scenario: Validate recent added item is present in the inventory
    Given the API at api-inventory is up and running
    When I send a GET request to inventory api
    Then the response status should be 200
    And the response should contain items which we added in add API
