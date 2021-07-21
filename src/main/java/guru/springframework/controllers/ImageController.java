package guru.springframework.controllers;

import guru.springframework.services.ImageService;
import guru.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Pierrot on 7/20/21.
 */
@Slf4j
@Controller
@RequestMapping("recipe")
public class ImageController {

    private final ImageService imageService;
    private final RecipeService recipeService;

    public ImageController(ImageService imageService, RecipeService recipeService) {
        this.imageService = imageService;
        this.recipeService = recipeService;
    }

    @GetMapping("{recipeId}/image")
    public String showUploadForm(@PathVariable Long recipeId, Model model) {
        model.addAttribute("recipe", recipeService.findById(recipeId));
        return "/recipe/imageuploadform";
    // Also Works!!!!
    // return "recipe/imageuploadform";
    }

    @PostMapping("{recipeId}/image")
    public String handleImage(@PathVariable Long recipeId, @Param("lksfsl") MultipartFile multipartFile) {
        imageService.saveImageFile(recipeId,multipartFile);
        return "redirect:/recipe/"+recipeId+"/show";
    }

}
