package guru.springframework.controllers;

import guru.springframework.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("recipes")
public class RecipeController {

    private final RecipeService recipeSrv;

    public RecipeController(RecipeService recipeSrv) {
        this.recipeSrv = recipeSrv;
    }

    @GetMapping("{id}")
    public String showRecipeById(@PathVariable Long id, Model model) {
        model.addAttribute("recipe", recipeSrv.getRecipeByID(id));
        return "/recipe/show";
    }
}
