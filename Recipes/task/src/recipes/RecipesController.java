package recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RecipesController {

    RecipesService recipesService;


    @Autowired
    public RecipesController(RecipesService recipesService) {
        this.recipesService = recipesService;
    }

    @GetMapping("/recipe/{id}")
    public Recipe getRecipe(@PathVariable Long id) {
        return recipesService.getRecipe(id);

    }

    @PostMapping("/register")
    public void registerUser(@Valid @RequestBody User user) {
        recipesService.registerUser(user);
    }

    @PostMapping("/recipe/new")
    public Map<String, Long> saveRecipe(@Valid @RequestBody Recipe recipe) {
        return recipesService.getSavedRecipeId(recipesService.saveRecipe(recipe));
    }

    @DeleteMapping("/recipe/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable Long id) {
        recipesService.deleteRecipe(id);
    }

    @PutMapping("/recipe/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void updateRecipe(@Valid @RequestBody Recipe recipe, @PathVariable Long id) {
         recipesService.updateRecipe(recipe, id);
    }

    @GetMapping("/recipe/search")
    public List<Recipe> searchRecipes(@RequestParam Map<String, String> allParams) {
        if (allParams.size() == 1 &&
                (allParams.containsKey("category") || allParams.containsKey("name"))) {
            return recipesService.searchRecipes(allParams);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
