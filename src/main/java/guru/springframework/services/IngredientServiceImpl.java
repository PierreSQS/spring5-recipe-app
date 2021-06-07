package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by jt on 6/28/17.
 */
@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final RecipeRepository recipeRepository;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand, RecipeRepository recipeRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {

        IngredientCommand ingredientCommand = null;

        Optional<Recipe> foundRecipeDBOpt = recipeRepository.findById(recipeId);

        if (foundRecipeDBOpt.isPresent()) {
            Recipe recipe = foundRecipeDBOpt.get();

            Optional<Ingredient> foundIngredientFromRecipeOpt = recipe.getIngredients().stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientId))
                    .findFirst();

            if(foundIngredientFromRecipeOpt.isPresent()){
                ingredientCommand = ingredientToIngredientCommand.convert(foundIngredientFromRecipeOpt.get());
            } else {
                //todo impl error handling
                log.error("Ingredient id not found: " + ingredientId);
            }

        } else  {
            //todo impl error handling
            log.error("recipe id not found. Id: " + recipeId);
        }


        return ingredientCommand;
    }
}
