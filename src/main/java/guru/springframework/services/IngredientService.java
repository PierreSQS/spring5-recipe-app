package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;

/**
 * Created by Pierrot on 6/9/21.
 */
public interface IngredientService {

    IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);

    IngredientCommand saveIngredientCommand(IngredientCommand command);
}
