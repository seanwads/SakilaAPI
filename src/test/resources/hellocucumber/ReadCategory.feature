Feature: Retrieve existing Category
  I would like to be able to get a category if it exists

  Scenario: GET existing category
    Given a Category exits with id 1
    When I call GET for Category 1
    Then I can see Category
