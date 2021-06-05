package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService{

    private final RecipeRepository recipeRepository;

    private final IngredientToIngredientCommand ingredientToIngredientCommand;

    public IngredientServiceImpl(RecipeRepository recipeRepository,
                                 IngredientToIngredientCommand ingredientToIngredientCommand) {
        this.recipeRepository = recipeRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        IngredientCommand ingredientCommand = null;

        Optional<Recipe> foundRecipeOpt = recipeRepository.findById(recipeId);

        if (foundRecipeOpt.isPresent()){
            Recipe recipeDB = foundRecipeOpt.get();
            Optional<Ingredient> foundIngredientOpt = recipeDB.getIngredients()
                    .stream().filter(ingr -> ingr.getId().equals(ingredientId))
                    .findFirst();

            if(foundIngredientOpt.isPresent()){
                ingredientCommand = ingredientToIngredientCommand.convert(foundIngredientOpt.get());
            }
        }

        return ingredientCommand;
    }
}
