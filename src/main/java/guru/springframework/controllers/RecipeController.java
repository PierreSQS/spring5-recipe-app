package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
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

    private static final String RECIPE_ATTR ="recipe";
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("{id}/show")
    public String showRecipeById(@PathVariable String id, Model model){

        model.addAttribute(RECIPE_ATTR, recipeService.findById(Long.valueOf(id)));
        return "recipe/showrecipe_by_id";
    }

    @GetMapping("new")
    public String showRecipeForm(RecipeCommand recipeCommand, Model model){
        model.addAttribute(RECIPE_ATTR, new RecipeCommand());
        return "recipe/recipeform";
    }

    @PostMapping
    public String saveOrUpdate(@ModelAttribute RecipeCommand recipeCommand) {
        RecipeCommand savedRecipeCmd = recipeService.saveRecipeCommand(recipeCommand);
        return "redirect:"+savedRecipeCmd.getId()+"/show";
    }

    @GetMapping("{id}/update")
    public String updateRecipe(@PathVariable Long id, Model model) {
        model.addAttribute(RECIPE_ATTR, recipeService.findCommandById(id));
        return "recipe/recipeform";
    }

    @GetMapping("{id}/delete")
    public String deleteRecipe(@PathVariable Long id) {
        recipeService.deleteById(id);
        return "redirect:/";
    }

}
