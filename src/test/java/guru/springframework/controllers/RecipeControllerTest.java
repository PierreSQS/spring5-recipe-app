package guru.springframework.controllers;

import guru.springframework.commands.NotesCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by jt on 6/19/17.
 */
public class RecipeControllerTest {

    private MockMvc mockMvc;

    @Mock
    RecipeService recipeService;

    RecipeController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        controller = new RecipeController(recipeService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testGetRecipe() throws Exception {

        Recipe recipe = new Recipe();
        recipe.setId(1L);

        when(recipeService.findById(anyLong())).thenReturn(recipe);

        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void testShowRecipeForm() throws Exception {
        mockMvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("recipe/recipeform"))
                .andDo(print());
    }

    @Test
    public void saveOrUpdateRecipe() throws Exception {

        NotesCommand notesCommand = new NotesCommand();
        notesCommand.setRecipeNotes("Recipe Notes");

        RecipeCommand recipeCommandMock = new RecipeCommand();
        recipeCommandMock.setId(1L);
        recipeCommandMock.setNotes(notesCommand);
        recipeCommandMock.setDescription("Recipe Description");

        // THIS IS WRONG!!! IT CAUSES THE NPE IN THE CONTROLLER
        when(recipeService.saveRecipeCommand(recipeCommandMock)).thenReturn(recipeCommandMock);

        // THIS IS CORRECT!!! IF YOU COMMENT OUT THIS LINE THE TEST WILL PASS!
        // when(recipeService.saveRecipeCommand(any())).thenReturn(recipeCommandMock);

        mockMvc.perform(post("/recipe"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:1"+"/show"))
                .andDo(print());
    }

}