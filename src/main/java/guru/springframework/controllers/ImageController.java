package guru.springframework.controllers;

import guru.springframework.services.ImageService;
import guru.springframework.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Modified by Pierrot on 7/24/21.
 */
@Controller
@RequestMapping("recipe")
public class ImageController {

    private final ImageService imageService;
    private final RecipeService recipeService;

    public ImageController(ImageService imageService, RecipeService recipeService) {
        this.imageService = imageService;
        this.recipeService = recipeService;
    }

    @GetMapping("{id}/image")
    public String showUploadForm(@PathVariable Long id, Model model){
        model.addAttribute("recipe", recipeService.findCommandById(id));

        return "recipe/imageuploadform";
    }

    @PostMapping("{id}/image")
    public String handleImagePost(@PathVariable Long id, @RequestParam("imagefile") MultipartFile file){

        imageService.saveImageFile(id, file);

        return "redirect:/recipe/" + id + "/show";
    }
}
