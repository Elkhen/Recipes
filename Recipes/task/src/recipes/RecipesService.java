package recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
public class RecipesService {

    private final RecipeRepository recipeRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public RecipesService(RecipeRepository recipeRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Recipe getRecipe(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Recipe saveRecipe(Recipe recipe) {
        recipe.setUser(userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
        return recipeRepository.save(recipe);
    }

    public Map<String, Long> getSavedRecipeId(Recipe recipe) {
        Long savedRecipeId = recipe.getId();
        return Map.of("id", savedRecipeId);
    }

    public void deleteRecipe(Long id) {
        Recipe recipe = getRecipe(id);
        checkAuth(id);
        recipeRepository.delete(recipe);
    }

    public void updateRecipe(Recipe recipe, Long id) {
        if (recipeRepository.existsById(id)) {
            checkAuth(id);
            recipe.setId(id);
            saveRecipe(recipe);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    public List<Recipe> searchRecipes(Map<String, String> allParams) {
        List<Recipe> foundRecipes;
        if (allParams.get("category") != null) {
            foundRecipes = recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(allParams.get("category"));
        } else {
            foundRecipes = recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc(allParams.get("name"));
        }
        return foundRecipes;
    }

    public void registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    private void checkAuth(long id) {
        if (!Objects.equals(recipeRepository.findById(id).get().getUser(),
                userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
