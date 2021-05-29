package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Created by jt on 6/19/17.
 */
@Controller
@RequestMapping("recipe")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/{id}/show")
    public String showRecipeById(@PathVariable String id, Model model){

        model.addAttribute("recipe", recipeService.findById(Long.valueOf(id)));
        return "recipe/show";
    }

    @GetMapping("/new")
    public String showRecipeForm(Model model) {
        model.addAttribute("recipe",new Recipe());
        return "recipe/RecipeForm";
    }

    @PostMapping
    public String saveOrUpdate(@ModelAttribute Recipe recipe) {
        recipeService.saveRecipe(recipe);
        return "redirect:/recipe/"+recipe.getId()+"/show/";
    }

}
