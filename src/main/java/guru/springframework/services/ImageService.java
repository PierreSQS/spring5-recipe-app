package guru.springframework.services;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Pierrot on 7/20/21.
 */
public interface ImageService {

    void saveImageFile(Long recipeId, MultipartFile file);
}
