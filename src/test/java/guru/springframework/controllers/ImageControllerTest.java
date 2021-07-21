package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.services.ImageService;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(ImageController.class)
public class ImageControllerTest {

    @MockBean
    ImageService imageSrvMock;

    @MockBean
    RecipeService recipeSrvMock;

    @Autowired
    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void showUploadForm() throws Exception {
        // Given
        Recipe recipeMock = new Recipe();
        recipeMock.setDescription("Recipe Mock");

        when(recipeSrvMock.findById(2L)).thenReturn(recipeMock);

        mockMvc.perform(get("/recipe/2/image"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("/recipe/imageuploadform"))
                .andExpect(content().string(containsString("<title>Image Upload Form</title>")))
                .andDo(print());
    }

    @Test
    public void handleImage() throws Exception {
        Long recipeID = 3L;

        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("imagefile","testing.txt", "text/plain",
                        "Spring Guru".getBytes());

        mockMvc.perform(multipart("/recipe/{recipeId}/image",recipeID).file(mockMultipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/"+recipeID+"/show"))
                .andExpect(header().string("Location",equalTo("/recipe/"+recipeID+"/show")))
                .andDo(print());

        verify(imageSrvMock).saveImageFile(anyLong(), any());
    }
}