package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.services.ImageService;
import guru.springframework.services.RecipeService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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

    @GetMapping("{id}/recipeimage")
    public void getImageFromDB(@PathVariable Long id, HttpServletResponse httpServletResp) throws IOException {
        RecipeCommand recipeCommandById = recipeService.findCommandById(id);

        Byte[] imageBytes = recipeCommandById.getImage();

        if(imageBytes != null) {
            byte[] byteArray = new byte[imageBytes.length];

            int i = 0;
            for(Byte wrappedByte :imageBytes) {
                byteArray[i++] = wrappedByte;
            }

            httpServletResp.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(byteArray);
            IOUtils.copy(is, httpServletResp.getOutputStream());
        }
    }
}
