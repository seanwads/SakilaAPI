package hellocucumber;

import com.example.Sakila.API.Controller.CategoryController;
import com.example.Sakila.API.Model.Category;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest
public class SpringIntegrationTest {
    @Autowired
    CategoryController categoryController;
    public Category getCategory(int id){
        return categoryController.getCatById(id);
    }
}
