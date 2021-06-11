package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Pierrot on 6/9/21.
 * Finalized on 6/11/2021.
 */
@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingrToIngrCmd;
    private final IngredientCommandToIngredient ingrCmdToIngr;
    private final RecipeRepository recipeRepository;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand, IngredientCommandToIngredient ingrCmdToIngr, RecipeRepository recipeRepository) {
        this.ingrToIngrCmd = ingredientToIngredientCommand;
        this.ingrCmdToIngr = ingrCmdToIngr;
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
                ingredientCommand = ingrToIngrCmd.convert(foundIngredientFromRecipeOpt.get());
            } else {
                //todo impl error handling. By JT is it really necessary?
                log.error("Ingredient id not found: " + ingredientId);
            }

        } else  {
            //todo impl error handling. By JT is it really necessary?
            log.error("recipe id not found. Id: " + recipeId);
        }


        return ingredientCommand;
    }

    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand ingrCmd) {
        IngredientCommand retIngrCmd = null;
        // 1- Search for associated Recipe in the DB
        Optional<Recipe> recipeByIdDBOpt = recipeRepository.findById(ingrCmd.getRecipeId());

        // 2a- In the solution, a Recipe will be created if not present!
        // Doesn't make sense for me not possible since we update from
        // the Recipe!!Thus not implemented. To check for future implementation.

        // 2b- If recipe present and it should be present because we update it from the form!!
        //     search the ingredient to update in the ingredients List of the recipe
        if (recipeByIdDBOpt.isPresent()) {
            // Get the recipe
            Recipe foundRecipeFromDB = recipeByIdDBOpt.get();

            // Get the ingredient to update from the ingredients list from recipe
            Optional<Ingredient> foundIngrOpt = foundRecipeFromDB.getIngredients().stream()
                    .filter(ingredient -> ingredient.getId().equals(ingrCmd.getId())).findFirst();

            if(foundIngrOpt.isPresent()) {
                // Search again the Recipe ingredient list to update the ingredient in the list
                foundRecipeFromDB.getIngredients().stream()
                        .filter(ingredient -> ingredient.getId().equals(ingrCmd.getId()))
                        .forEach(ingredient -> {
                            ingredient.setDescription(ingrCmd.getDescription());
                            ingredient.setAmount(ingrCmd.getAmount());
                            // theoretically an NPE possible on ingrCmd, but in fact
                            // not possible to me since we update from the existing Recipe!!
                            ingredient.setUom(ingrCmdToIngr.convert(ingrCmd).getUom());
                        });

                // 3- save the updated recipe (with the updated ingredient)
                recipeRepository.save(foundRecipeFromDB);

                // 4 - produce the ingredientCommand
                retIngrCmd = ingrToIngrCmd.convert(foundIngrOpt.get());


            } // else do nothing


        } // else not possible to me since we update from the existing Recipe!! To check!!!!

        return retIngrCmd;
    }
}
