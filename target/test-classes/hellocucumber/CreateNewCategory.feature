Feature: Create a new Category
  I would like to be able to create new categories with custom names

  Scenario: Create new Category
    Given a name parameter "Indie"
    When I create a new Category
    Then a Category exists with name