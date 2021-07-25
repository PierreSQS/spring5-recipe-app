package guru.springframework.services;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

/**
 * Modified by Pierrot on 7/25/21.
 */
@Slf4j
@Service
public class ImageServiceImpl implements ImageService {
    private final RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void saveImageFile(Long recipeId, MultipartFile file){
        Optional<Recipe> optByID = recipeRepository.findById(recipeId);

        byte[] bytes = new byte[0];
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            log.error("Erreur by reading the image: {}",e.getMessage());
            e.printStackTrace();
        }

        Byte[] imageBytes = new Byte[bytes.length];

        int i = 0;
        for ( byte fileByte : bytes) {
            imageBytes[i++] = fileByte;
        }

        Recipe foundRecipe = optByID.orElseThrow(() -> new RuntimeException("Recipe not found!!!"));
        foundRecipe.setImage(imageBytes);

        recipeRepository.save(foundRecipe);

        log.debug("###### Image saved!!! ########");

    }
}
