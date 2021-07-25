package guru.springframework.services;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Modified by Pierrot on 7/25/21.
 */
@RunWith(MockitoJUnitRunner.class)
public class ImageServiceImplTest {

    @Mock
    RecipeRepository recipeRepoMock;

    @InjectMocks
    ImageServiceImpl imageService;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void saveImageFile() throws IOException {
        // Given
        final Long recipeId = 1L;
        MultipartFile multipartFile =
                new MockMultipartFile("imagefile", "testing.txt", "text/plain",
                                        "Spring Framework Guru".getBytes());

        Recipe recipeMock = new Recipe();
        recipeMock.setId(recipeId);
        recipeMock.setDescription("Recipe Mock");

        when(recipeRepoMock.findById(anyLong())).thenReturn(Optional.of(recipeMock));

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        // When
        imageService.saveImageFile(recipeId, multipartFile);

        // Then
        verify(recipeRepoMock).save(argumentCaptor.capture());
        Recipe  savedRecipe = argumentCaptor.getValue();
        assertThat(savedRecipe.getImage()).hasSameSizeAs(multipartFile.getBytes());

    }
}