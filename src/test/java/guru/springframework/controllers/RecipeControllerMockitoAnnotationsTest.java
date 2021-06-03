package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Pierrot on 6/3/21.
 */
@RunWith(MockitoJUnitRunner.class)
public class RecipeControllerMockitoAnnotationsTest {

    private MockMvc mockMvc;

    @Mock
    RecipeService recipeService;

    @InjectMocks
    RecipeController controller;

    @Before
    public void setUp() throws Exception {

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testGetRecipe() throws Exception {

        Recipe recipe = new Recipe();
        recipe.setId(1L);

        when(recipeService.findById(anyLong())).thenReturn(recipe);

        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/showrecipe_by_id"))
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
    public void testSaveOrUpdate() throws Exception {

        RecipeCommand recipeCmdMock = new RecipeCommand();
        recipeCmdMock.setId(3L);

        when(recipeService.saveRecipeCommand(any())).thenReturn(recipeCmdMock);

        mockMvc.perform(post("/recipe"))
                .andExpect(status().is3xxRedirection())
                // In this case Model-Attribute generated
                // but not in the test with @WebMvcTest?!?
                .andExpect(model().attributeExists("recipeCommand"))
                .andExpect(view().name("redirect:3/show"))
                .andDo(print());
    }
}