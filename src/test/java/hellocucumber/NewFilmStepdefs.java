package hellocucumber;

import com.example.Sakila.API.Model.Category;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NewFilmStepdefs extends SpringIntegrationTest {
    Category category;
    private final String catName = "cucumber test cat";

//    private String expectedName;
//
//    @Given("a name parameter {string}")
//    public void aNameParameter(String arg0) {
//        expectedName = arg0;
//    }
//    @When("I create a new Category")
//    public void iCreateANewCategory() {
//        newCat = new Category(expectedName);
//    }
//
//    @Then("a Category exists with name")
//    public void aCategoryExistsWithName() {
//        assertEquals(expectedName, newCat.getName());
//    }

    @Given("a Category exits with id {int}")
    public void aCategoryExitsWithId(int arg0) {
        assertNotNull(getCategory(arg0));
    }
    @When("I call GET for Category {int}")
    public void iCallGETForCategory(int arg0) {

    }

    @Then("I can see Category")
    public void iCanSeeCategory() {
        //check response
    }



}

